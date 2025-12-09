package uk.ac.cf.spring.clientprojectteam3.teams;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeamController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ControllerOnlyTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TeamService teamService;

    @Test
    public void shouldShowCreateForm() throws Exception {
        MvcResult result = mvc
                .perform(get("/teams/create"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("team"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertTrue(content.contains("Create A New Team"));
        assertTrue(content.contains("Team Name"));
        assertTrue(content.contains("Description"));
    }

    @Test
    public void shouldSubmitAndRedirect() throws Exception {

        mvc.perform(post("/teams/create")
                .param("team.teamName", "Team1")
                .param("team.teamDescription", "Team1 Description"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teams"));
    }

    @Test
    public void shouldDisplayTeamsWhenUserIsInTeams() throws Exception {

        List<UserTeam> teams = List.of(
                new UserTeam(1L, "Team 1", true, 2L),
                new UserTeam(2L, "Team 2", false, 4L));

        when(teamService.listOfTeamsForCurrentUser()).thenReturn(teams);

        MvcResult result = mvc
                .perform(get("/teams"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("teams/teams-list"))
                .andExpect(model().attributeExists("teams"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertTrue(content.contains("Team 1"));
        assertTrue(content.contains("Team 2"));
        assertTrue(content.contains("<ul class=\"list-group list-group-flush bg-white m-0\">"));
    }

    @Test
    public void shouldShowWarningUserInNoTeams() throws Exception {
        when(teamService.listOfTeamsForCurrentUser()).thenReturn(List.of());

        MvcResult result = mvc
                .perform(get("/teams"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("teams/teams-list"))
                .andExpect(model().attributeExists("teams"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertFalse(content.contains("<ul class=\"list-group list-group-flush bg-white m-0\">"));
        assertTrue(content.contains("<div class=\"warning alert alert-info\">"));
        assertTrue(content.contains("You are not currently a member of any teams."));
    }
}

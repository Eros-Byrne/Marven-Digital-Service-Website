package uk.ac.cf.spring.clientprojectteam3.teams;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.ac.cf.spring.clientprojectteam3.capabilities.Outcome;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
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
                new UserTeam(1L, "Team 1", true, 2L,0L),
                new UserTeam(2L, "Team 2", false, 4L, 0L));

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
    public void shouldErrorAtWrongCode() throws Exception {
        when(teamService.listOfTeamsForCurrentUser()).thenReturn(List.of());

        MvcResult result = mvc
                .perform(post("/teams/join?joinCode=12345"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teams"))
                .andExpect(flash().attributeExists("joinError"))
                .andReturn();
    }

    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            long d = Long.parseLong(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Test
    public void shouldReturnJoinCode() throws Exception {
        when(teamService.listOfTeamsForCurrentUser()).thenReturn(List.of(new UserTeam(1L, "Team 1", true, 192647285L,0L)));

        MvcResult result = mvc
                .perform(get("/teams/joinCode/1"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("192647285", result.getResponse().getContentAsString());

        MvcResult result2 = mvc
                .perform(get("/teams/joinCode/2"))
                .andExpect(status().isForbidden())
                .andReturn();

        assertEquals("-1", result2.getResponse().getContentAsString());
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
        assertTrue(content.contains("<div id=\"no-teams-warning\" class=\"warning alert alert-info\">"));
        assertTrue(content.contains("You are not currently a member of any teams."));
    }

//    AI assisted with the tests below :)

    @Test
    public void shouldRenderSingleTeamWithDefaultOutcome() throws Exception {
        Long teamId = 27L;

        TeamDetails details = new TeamDetails(teamId, "Team 27", "Team desc", 123456789L, List.of(), List.of());

        when(teamService.getTeamDetailsForTeam(teamId)).thenReturn(details);
        when(teamService.isTheCurrentUserAManager(teamId)).thenReturn(true);

        Outcome outcome1 = new Outcome(10L, "Outcome 10", List.of());
        Outcome outcome2 = new Outcome(11L, "Outcome 11", List.of());
        when(teamService.listEnabledOutcomes()).thenReturn(List.of(outcome1, outcome2));

        List<TopMemberForOutcome> leaders = List.of(new TopMemberForOutcome(7L, "John", "John@email.com", 87.0));
        when(teamService.getTopMembersForOutcome(teamId, outcome1.getId())).thenReturn(leaders);

        MvcResult result = mvc.perform(get("/teams/{id}", teamId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("teams/single-team-page"))
                .andExpect(model().attributeExists("teamDetails"))
                .andExpect(model().attributeExists("isManager"))
                .andExpect(model().attributeExists("outcomes"))
                .andExpect(model().attributeExists("selectedOutcomeId"))
                .andExpect(model().attributeExists("leaders"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Top Members by Outcome"));
        assertTrue(content.contains("John"));
    }

    @Test
    public void shouldRenderSingleTeamWithFilter() throws Exception {
        Long teamId = 27L;
        Long outcomeId = 10L;

        TeamDetails details = new TeamDetails(teamId, "Team 27", "Team desc", 123456789L, List.of(), List.of());

        when(teamService.getTeamDetailsForTeam(teamId)).thenReturn(details);
        when(teamService.isTheCurrentUserAManager(teamId)).thenReturn(true);

        Outcome outcome1 = new Outcome(10L, "Outcome 10", List.of());
        Outcome outcome2 = new Outcome(11L, "Outcome 11", List.of());
        when(teamService.listEnabledOutcomes()).thenReturn(List.of(outcome1, outcome2));

        List<TopMemberForOutcome> leaders = List.of(new TopMemberForOutcome(7L, "John", "John@email.com", 87.0));
        when(teamService.getTopMembersForOutcome(teamId, outcome1.getId())).thenReturn(leaders);

        MvcResult result = mvc.perform(get("/teams/{id}", teamId)
                        .param("outcomeId", String.valueOf(outcomeId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("teams/single-team-page"))
                .andExpect(model().attribute("selectedOutcomeId", outcomeId))
                .andExpect(model().attributeExists("leaders"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("John"));
    }

    @Test
    public void shouldPromoteMemberAndRedirect() throws Exception {
        Long teamId = 27L;
        Long memberId = 10L;

        mvc.perform(post("/teams/{teamId}/members/{memberId}/promote", teamId, memberId))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teams/" + teamId));

        verify(teamService).promoteTeamMember(teamId, memberId);
    }

//    My code :)
    @Test
    public void shouldDemoteManagerAndRedirect() throws Exception {
        Long teamId = 27L;
        Long memberId = 10L;

        mvc.perform(post("/teams/{teamId}/members/{memberId}/demote", teamId, memberId))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teams/" + teamId));

        verify(teamService).demoteManager(teamId, memberId);
    }

//    Ai code for this test :)
    @Test
    public void shouldFailToDemoteLastManager() throws Exception {
        Long teamId = 27L;
        Long memberId = 10L;

        doThrow(new IllegalStateException("A team can't have 0 managers")).when(teamService).demoteManager(teamId, memberId);

        mvc.perform(post("/teams/{teamId}/members/{memberId}/demote", teamId, memberId))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teams/" + teamId));


    }
}

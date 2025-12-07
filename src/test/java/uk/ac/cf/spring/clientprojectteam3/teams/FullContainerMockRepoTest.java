package uk.ac.cf.spring.clientprojectteam3.teams;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", roles = {"USER"})
public class FullContainerMockRepoTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TeamRepository teamRepo;

    @MockitoBean
    private UserService currentUserService;

    @Test
    public void createTeamShouldCallBothRepoFunctionsAndRedirect() throws Exception {

        when(teamRepo.createTeam(any(NewTeam.class))).thenReturn(2L);
        when(currentUserService.getCurrentUserId()).thenReturn(1);


        // redirected url needs to be changed to /teams once that has been created
        mvc.perform(post("/teams/create")
                        .with(csrf())
                        .param("team.teamName", "Team 1")
                        .param("team.teamDescription", "Team Description 1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teams"));

        verify(teamRepo).createTeam(any(NewTeam.class));
        verify(currentUserService).getCurrentUserId();
        verify(teamRepo).setUserAsManager(1L, 2L);
    }

    @Test
    public void getTeamsShouldRenderListWhenUserInTeams() throws Exception {

        when(currentUserService.getCurrentUserId()).thenReturn(1);

        List<UserTeam> teams = List.of(
                new UserTeam(1L, "Team 1", false, 7L),
                new UserTeam(2L, "Team 2", false, 27L)
        );

        when(teamRepo.getAllTeamsForAUser(1L)).thenReturn(teams);

        MvcResult result = mvc
                .perform(get("/teams"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertTrue(content.contains("Member"));
        assertTrue(content.contains("Team 1"));
        assertTrue(content.contains("Team 2"));
    }

}

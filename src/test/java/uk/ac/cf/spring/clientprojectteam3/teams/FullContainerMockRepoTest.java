package uk.ac.cf.spring.clientprojectteam3.teams;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.cf.spring.clientprojectteam3.security.CurrentUserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FullContainerMockRepoTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TeamRepository teamRepo;

    @MockitoBean
    private CurrentUserService currentUserService;

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
                .andExpect(redirectedUrl("/"));

        verify(teamRepo).createTeam(any(NewTeam.class));
        verify(currentUserService).getCurrentUserId();
        verify(teamRepo).setUserAsManager(1L, 2L);
    }

}

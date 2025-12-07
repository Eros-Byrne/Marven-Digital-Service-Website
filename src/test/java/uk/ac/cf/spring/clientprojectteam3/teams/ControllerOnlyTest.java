package uk.ac.cf.spring.clientprojectteam3.teams;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
                .andExpect(redirectedUrl("/"));
    }
}

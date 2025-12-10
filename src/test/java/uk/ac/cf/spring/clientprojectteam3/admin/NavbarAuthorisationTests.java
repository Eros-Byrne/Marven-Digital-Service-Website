package uk.ac.cf.spring.clientprojectteam3.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.cf.spring.clientprojectteam3.user.HomePageController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomePageController.class)
public class NavbarAuthorisationTests {

    @Autowired
    private MockMvc mockMvc;

    // --------------------- Anonymous User ---------------------
    @Test
    @WithMockUser()
    void navbarForAnonymous() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Home")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Quiz")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Summary")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Teams")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Outcomes")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Login")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Dashboard"))));
    }

    // --------------------- Authenticated Non-Admin ---------------------
    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void navbarForUser() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Home")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Quiz")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Summary")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Teams")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Outcomes")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Settings")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Logout")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Dashboard"))));
    }

    // --------------------- Admin User ---------------------
    @Test
    @WithMockUser(username = "admin@example.com", roles = {"USER", "ADMIN"})
    void navbarForAdmin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Home")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Dashboard")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Edit Outcomes")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Edit Quizzes")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Edit Capabilities")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Settings")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Logout")));
    }
}

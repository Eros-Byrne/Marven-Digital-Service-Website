package uk.ac.cf.spring.clientprojectteam3.settings;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import uk.ac.cf.spring.clientprojectteam3.user.User;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SettingsController.class)
class SettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(username = "test@email.com")
    void settings_page_loads_when_authenticated() throws Exception {

        User user = new User(
                1,
                "Test User",
                "test@email.com",
                "07123456789",
                "encodedPassword"
        );

        when(userService.findByEmail("test@email.com"))
                .thenReturn(user);

        mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(view().name("settings"))
                .andExpect(model().attributeExists("user"));
    }
    @Test
    void settings_redirects_to_login_when_unauthenticated() throws Exception {
        mockMvc.perform(get("/settings"))
                .andExpect(status().is4xxClientError());
    }
    }

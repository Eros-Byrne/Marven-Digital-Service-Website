package uk.ac.cf.spring.clientprojectteam3.settings;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.cf.spring.clientprojectteam3.security.CustomUserDetails;
import uk.ac.cf.spring.clientprojectteam3.user.User;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SettingsController.class)
@AutoConfigureMockMvc(addFilters = false)
class SettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SecurityContextLogoutHandler securityContextLogoutHandler;

    @MockitoBean
    private UserService userService;

    private User testUser() {
        return new User(
                1,
                "Test User",
                "test@email.com",
                "07123456789",
                "encodedPassword",
                "job role"
        );
    }

    @Test
    @WithMockUser(username = "test@email.com")
    void settings_page_loads_when_authenticated() throws Exception {
        when(userService.findByEmail("test@email.com"))
                .thenReturn(testUser());

        mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(view().name("/login/settings"))
                .andExpect(model().attributeExists("user"));
    }


    @Test
    @WithMockUser(username = "test@email.com")
    void change_phone_updates_and_redirects() throws Exception {
        when(userService.findByEmail("test@email.com"))
                .thenReturn(testUser());

        mockMvc.perform(post("/settings/change-phone")
                        .param("newPhone", "07123456789"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings"));

        verify(userService).updatePhone(1, "07123456789");
    }

    @Test
    @WithMockUser(username = "test@email.com")
    void change_password_fails_when_current_password_incorrect() throws Exception {
        User user = testUser();

        when(userService.findByEmail("test@email.com"))
                .thenReturn(user);
        when(userService.checkPassword(user, "wrongPass"))
                .thenReturn(false);

        mockMvc.perform(post("/settings/change-password")
                        .param("currentPassword", "wrongPass")
                        .param("newPassword", "newPassword123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings"));

        verify(userService, never())
                .updatePassword(any(), any());
    }

    @Test
    @WithMockUser(username = "test@email.com")
    void change_password_success_when_current_password_correct() throws Exception {
        User user = testUser();

        when(userService.findByEmail("test@email.com"))
                .thenReturn(user);
        when(userService.checkPassword(user, "oldPassword"))
                .thenReturn(true);

        mockMvc.perform(post("/settings/change-password")
                        .param("currentPassword", "oldPassword")
                        .param("newPassword", "newPassword123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?passwordChanged"));

        verify(userService)
                .updatePassword(1, "newPassword123");
    }
}

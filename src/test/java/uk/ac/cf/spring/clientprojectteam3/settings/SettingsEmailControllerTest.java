package uk.ac.cf.spring.clientprojectteam3.settings;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SettingsController.class)
public class SettingsEmailControllerTest {

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
    void change_email_successful_when_not_taken() throws Exception {
        User user = testUser();
        CustomUserDetails customUser = new CustomUserDetails(
                "test@email.com",
                "password",
                "testUser",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());

        when(userService.findByEmail("test@email.com"))
                .thenReturn(user);
        when(userService.findByEmail("new@email.com"))
                .thenReturn(null);

        mockMvc.perform(post("/settings/change-email")
                        .with(authentication(authToken))
                        .with(csrf())
                        .param("newEmail", "new@email.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings"));

        verify(userService).updateEmail(1, "new@email.com");
    }

    @Test
    @WithMockUser(username = "test@email.com")
    void change_email_fails_when_email_already_exists() throws Exception {
        User user = testUser();
        User existing = new User();
        existing.setUserid(99);
        CustomUserDetails customUser = new CustomUserDetails(
                "test@email.com",
                "password",
                "testUser",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());


        when(userService.findByEmail("test@email.com"))
                .thenReturn(user);
        when(userService.findByEmail("taken@email.com"))
                .thenReturn(existing);

        mockMvc.perform(post("/settings/change-email")
                        .with(authentication(authToken))
                        .with(csrf())
                        .param("newEmail", "taken@email.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings"));

        verify(userService, never())
                .updateEmail(any(), any());
    }
}

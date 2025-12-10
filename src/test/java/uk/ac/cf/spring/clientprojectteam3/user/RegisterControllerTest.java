package uk.ac.cf.spring.clientprojectteam3.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegisterController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void register_page_loads() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/register"))
                .andExpect(model().attributeExists("user"))
                .andExpect(content().string(containsString("Create Account")));
    }

    @Test
    void user_can_register_successfully() throws Exception {

        when(userService.findByEmail("test@email.com"))
                .thenReturn(null);

        mockMvc.perform(post("/register")
                        .param("name", "Test User")
                        .param("email", "test@email.com")
                        .param("phone", "07123456789")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        verify(userService).registerUser(any(User.class));
    }

    @Test
    void registration_fails_when_email_exists() throws Exception {

        when(userService.findByEmail("test@email.com"))
                .thenReturn(new User());

        mockMvc.perform(post("/register")
                        .param("name", "Test User")
                        .param("email", "test@email.com")
                        .param("phone", "07123456789")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/register"))
                .andExpect(model().attributeExists("emailError"));
    }
}
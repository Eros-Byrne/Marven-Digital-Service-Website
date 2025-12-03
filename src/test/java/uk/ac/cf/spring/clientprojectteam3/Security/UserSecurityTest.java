package uk.ac.cf.spring.clientprojectteam3.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void quiz_requires_authentication() throws Exception {
        mockMvc.perform(get("/quiz/1/attempt/0/question/0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void settings_requires_authentication() throws Exception {
        mockMvc.perform(get("/settings"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void quiz_list_is_public() throws Exception {
        mockMvc.perform(get("/quiz-list"))
                .andExpect(status().isOk());
    }

    @Test
    void login_page_is_public() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }
}

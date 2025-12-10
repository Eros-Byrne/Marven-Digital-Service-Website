package uk.ac.cf.spring.clientprojectteam3.settings;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    @Test
    void constraint_violation_redirects_with_flash_error() throws Exception {

        MockMvc mockMvc =
                MockMvcBuilders
                        .standaloneSetup(new TestController())
                        .setControllerAdvice(new GlobalExceptionHandler())
                        .build();

        mockMvc.perform(get("/test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings"))
                .andExpect(flash().attribute("error", "Validation failed"));
    }

    @RestController
    static class TestController {

        @GetMapping("/test")
        public void triggerConstraintViolation() {

            ConstraintViolation<?> violation =
                    mock(ConstraintViolation.class);
            when(violation.getMessage())
                    .thenReturn("Validation failed");

            throw new ConstraintViolationException(Set.of(violation));
        }
    }
}

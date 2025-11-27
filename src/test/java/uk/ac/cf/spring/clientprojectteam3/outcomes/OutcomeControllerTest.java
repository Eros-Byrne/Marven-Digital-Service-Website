package uk.ac.cf.spring.clientprojectteam3.outcome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(OutcomeController.class)
public class OutcomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OutcomeService outcomeService;

    private List<Outcome> testOutcomes;

    @BeforeEach
    void setUp() {
        testOutcomes = Arrays.asList(
                new Outcome(1L, "Career Exploration",
                        Arrays.asList("Research different career options", "Understand job market trends")),
                new Outcome(2L, "Skill Development",
                        Arrays.asList("Learn new technical skills", "Develop communication abilities")),
                new Outcome(3L, "Professional Network",
                        Arrays.asList("Attend networking events", "Build LinkedIn presence", "Join professional groups"))
        );
    }

    @Test
    void testShowOutcomesPage() throws Exception {
        when(outcomeService.getAllOutcomes()).thenReturn(testOutcomes);

        mockMvc.perform(get("/outcomes"))
                .andExpect(status().isOk())
                .andExpect(view().name("outcomes"))
                .andExpect(model().attributeExists("outcomes"))
                .andExpect(model().attribute("outcomes", hasSize(3)));
    }

    @Test
    void testOutcomesPageContainsCorrectData() throws Exception {
        when(outcomeService.getAllOutcomes()).thenReturn(testOutcomes);

        mockMvc.perform(get("/outcomes"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("outcomes", hasItem(
                        hasProperty("title", is("Career Exploration"))
                )));
    }

    @Test
    void testOutcomesPageWithEmptyList() throws Exception {
        when(outcomeService.getAllOutcomes()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/outcomes"))
                .andExpect(status().isOk())
                .andExpect(view().name("outcomes"))
                .andExpect(model().attribute("outcomes", hasSize(0)));
    }

    @Test
    void testOutcomesPageContainsCapabilities() throws Exception {
        when(outcomeService.getAllOutcomes()).thenReturn(testOutcomes);

        mockMvc.perform(get("/outcomes"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("outcomes", hasItem(
                        hasProperty("capabilities", hasSize(2))
                )))
                .andExpect(model().attribute("outcomes", hasItem(
                        hasProperty("capabilities", hasItems("Research different career options", "Understand job market trends"))
                )));
    }
}
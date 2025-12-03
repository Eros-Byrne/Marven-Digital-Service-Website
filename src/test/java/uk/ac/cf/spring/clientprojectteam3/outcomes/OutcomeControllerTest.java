package uk.ac.cf.spring.clientprojectteam3.outcomes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.cf.spring.clientprojectteam3.capabilities.Capability;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityController;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityService;
import uk.ac.cf.spring.clientprojectteam3.capabilities.Outcome;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(CapabilityController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OutcomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CapabilityService capabilityService;

    private List<Outcome> testOutcomes;

    @BeforeEach
    void setUp() {
        testOutcomes = Arrays.asList(
                new Outcome(1L, "Career Exploration",
                        Arrays.asList(
                                new Capability(1L, "Research different career options", "Description"))),
                new Outcome(2L, "Skill Development",
                        Arrays.asList(
                                new Capability(2L, "Learn new technical skills", "Description"))),
                new Outcome(3L, "Professional Network",
                        Arrays.asList(
                                new Capability(3L, "Attend networking events", "Description"))));
    }

    @Test
    void testShowOutcomesPage() throws Exception {
        when(capabilityService.getAllOutcomes()).thenReturn(testOutcomes);

        mockMvc.perform(get("/outcomes"))
                .andExpect(status().isOk())
                .andExpect(view().name("capabilities/outcomes"))
                .andExpect(model().attributeExists("outcomes"))
                .andExpect(model().attribute("outcomes", hasSize(3)));
    }

    @Test
    void testOutcomesPageContainsCorrectData() throws Exception {
        when(capabilityService.getAllOutcomes()).thenReturn(testOutcomes);

        mockMvc.perform(get("/outcomes"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("outcomes", hasItem(
                        hasProperty("title", is("Career Exploration"))
                )));
    }

    @Test
    void testOutcomesPageWithEmptyList() throws Exception {
        when(capabilityService.getAllOutcomes()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/outcomes"))
                .andExpect(status().isOk())
                .andExpect(view().name("capabilities/outcomes"))
                .andExpect(model().attribute("outcomes", hasSize(0)));
    }

    @Test
    void testOutcomesPageContainsCapabilities() throws Exception {
        when(capabilityService.getAllOutcomes()).thenReturn(testOutcomes);

        mockMvc.perform(get("/outcomes"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("outcomes", hasItem(
                        hasProperty("capabilities", hasSize(1))
                )))
                .andExpect(model().attribute("outcomes", hasItem(
                        hasProperty("capabilities", hasItem(hasProperty("title", equalTo("Research different career options"))))
                )));
    }
}
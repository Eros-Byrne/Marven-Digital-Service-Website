package uk.ac.cf.spring.clientprojectteam3.admin.outcomes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.ac.cf.spring.clientprojectteam3.admin.outcome.AdminOutcome;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "Test user", roles = {"USER", "ADMIN"})
public class FullContainerMockRepoTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CapabilityRepository capRepo;

    @Test
    public void outcomesListDisplaysCorrectDataFromRepo() throws Exception {
        when(capRepo.findAllOutcomesWithNumberOfCapabilities()).thenReturn(List.of(
                new AdminOutcome(1L, "outcome 1", 29),
                new AdminOutcome(2L, "Outcome 2", 123)
        ));

        MvcResult result = mvc
                .perform(get("/admin/outcomes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertTrue(content.contains("<p class=\"mb-0 text-truncate text-on-white-background\">outcome 1</p>"));
        assertTrue(content.contains("<p class=\"mb-0 text-truncate text-on-white-background\">Outcome 2</p>"));
    }

    @Test
    public void createOutcomeCallsRepoAndRedirects() throws Exception {

        mvc.perform(post("/admin/outcomes/add")
                        .with(csrf())
                        .param("title", "Test Outcome"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/outcomes"));

        verify(capRepo).CreateOutcome("Test Outcome");
    }

    @Test
    public void deleteOutcomeCallsRepoAndRedirects() throws Exception {

        mvc.perform(post("/admin/outcomes/delete/2")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/outcomes"));

        verify(capRepo).deleteOutcome(2L);
    }
}

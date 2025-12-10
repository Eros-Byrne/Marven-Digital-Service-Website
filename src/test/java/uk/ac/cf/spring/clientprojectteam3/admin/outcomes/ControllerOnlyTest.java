package uk.ac.cf.spring.clientprojectteam3.admin.outcomes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.ac.cf.spring.clientprojectteam3.admin.outcome.AdminOutcome;
import uk.ac.cf.spring.clientprojectteam3.admin.outcome.AdminOutcomeController;
import uk.ac.cf.spring.clientprojectteam3.admin.outcome.AdminOutcomeService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminOutcomeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ControllerOnlyTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AdminOutcomeService adminOutcomeService;

    @Test
    public void outcomesAreRenderedInList() throws Exception {
        when(adminOutcomeService.getAllOutcomesWithCapabilityCount()).thenReturn(List.of(
                new AdminOutcome(1L, "Outcome 1", 27),
                new AdminOutcome(2L, "Outcome 2", 38)
        ));

        MvcResult result = mvc
                .perform(get("/admin/outcomes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/outcomes-list"))
                .andExpect(model().attributeExists("outcomes"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertTrue(content.contains("<ul class=\"list-group list-group-flush grey-background-colour list-rounded\">"));
    }

    @Test
    public void createOutcomeFormRenders() throws Exception {

        MvcResult result= mvc
                .perform(get("/admin/outcomes/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/create-outcome"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertTrue(content.contains("<div class=\"card grey-background-colour p-2\" id=\"create-outcome-card\">"));
        assertTrue(content.contains("<input type=\"text\" name=\"title\" class=\"form-control\" placeholder=\"Outcome title\" required>"));
    }

    @Test
    public void createOutcomeRedirectsAndCallsServiceMethods() throws Exception {

        mvc.perform(post("/admin/outcomes/add")
                    .param("title", "Outcome 1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/outcomes"));

        verify(adminOutcomeService).createOutcome("Outcome 1");
    }

    @Test
    public void deleteCallsServiceAndRedirects() throws Exception {

        mvc.perform(post("/admin/outcomes/delete/4"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/outcomes"));

        verify(adminOutcomeService).deleteOutcome(4L);
    }
}

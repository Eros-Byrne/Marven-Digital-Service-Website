package uk.ac.cf.spring.clientprojectteam3.capability;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FullContainerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void pageShouldLoadSuccessfully() throws Exception {

        MvcResult result = mvc.
                perform(get("/capability/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("capabilities/single_capability"))
                .andExpect(model().attributeExists("capability", "resources", "skills"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // has title
        assertTrue(content.contains("<h1 class=\"white-text\">"));

        //has resources table
        assertTrue(content.contains("<th id=\"resource-col\" class=\"p-1 text-on-white-background\">Resource</th>"));
        assertTrue(content.contains("<th id=\"difficulty-col\" class=\"p-1 text-on-white-background\">Difficulty</th>"));

        //has skills card
        assertTrue(content.contains("<h4 class=\"card-title text-center\">Transferable Skills</h4>"));
    }

    @Test
    public void pageShouldRedirectToOutcomesIfInvalidId() throws Exception {
        mvc.perform(get("/capability/99999999"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/outcomes"));
    }

}

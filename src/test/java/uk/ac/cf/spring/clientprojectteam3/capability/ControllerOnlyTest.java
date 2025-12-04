package uk.ac.cf.spring.clientprojectteam3.capability;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.ac.cf.spring.clientprojectteam3.capabilities.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CapabilityController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ControllerOnlyTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CapabilityService capabilityService;

    @Test
    public void shouldDisplayCapabilityPageWithMockedData() throws Exception {
        Capability capability = new Capability(1L, "Mock Title", "Mock Description");
        List<Resource> resources = List.of(
                new Resource(1L, "Mock Resource Content", Difficulty.Low),
                new Resource(2L, "Mock Resource Content 2", Difficulty.High));
        List<Skill> skills = List.of(
                new Skill(1L, "Skill1"),
                new Skill(2L, "Skill2"));

        when(capabilityService.getCapability(anyLong())).thenReturn(capability);
        when(capabilityService.getResources(anyLong())).thenReturn(resources);
        when(capabilityService.getSkills(anyLong())).thenReturn(skills);

        MvcResult result = mvc
                .perform(get("/capability/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("capabilities/single_capability"))
                .andExpect(model().attribute("capability", capability))
                .andExpect(model().attribute("resources", resources))
                .andExpect(model().attribute("skills", skills))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Tests the capability values are in the html
        assertTrue(content.contains("<h1 class=\"white-text\">Mock Title</h1>"));
        assertTrue(content.contains("<p id=\"capability_description\" " +
                "class=\"border rounded border-2 p-2 mt-2 text-on-white-background\">" +
                "Mock Description</p>"));

        // Tests the mock resources are in the table
        //Resource 1
        assertTrue(content.contains("<td class=\"p-1 text-on-white-background\">Mock Resource Content</td>"));
        assertTrue(content.contains("<td class=\"p-1 text-on-white-background\">Low</td>"));

        //Resource 2
        assertTrue(content.contains("<td class=\"p-1 text-on-white-background\">Mock Resource Content 2</td>"));
        assertTrue(content.contains("<td class=\"p-1 text-on-white-background\">High</td>"));

        //Skills
        assertTrue(content.contains("<li class=\"list-group-item\">Skill1</li>"));
        assertTrue(content.contains("<li class=\"list-group-item\">Skill2</li>"));
    }

    @Test
    public void redirectsIfInvalidCapabilityId() throws Exception {

        when(capabilityService.getCapability(12345L)).thenThrow(
                new IllegalArgumentException("Invalid capability id"));

        mvc.perform(get("/capability/12345"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/outcomes"));

    }

    @Test
    public void pageStillLoadsIfNoResources() throws Exception {
        when(capabilityService.getResources(anyLong())).thenReturn(List.of());
        when(capabilityService.getCapability(anyLong())).thenReturn(
                new Capability(1L, "Mock Title", "Mock Description"));
        when(capabilityService.getSkills(anyLong())).thenReturn(List.of(
                new Skill(1L, "Skill1")));

        mvc.perform(get("/capability/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("capabilities/single_capability"));
    }

    @Test
    public void pageStillLoadsIfNoSkills() throws Exception {
        when(capabilityService.getResources(anyLong())).thenReturn(List.of(
                new Resource(1L, "Mock Resource Content", Difficulty.High)));
        when(capabilityService.getSkills(anyLong())).thenReturn(List.of());
        when(capabilityService.getCapability(anyLong())).thenReturn(
                new Capability(1L, "Mock Title", "Mock Description"));

        mvc.perform(get("/capability/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("capabilities/single_capability"));
    }
}

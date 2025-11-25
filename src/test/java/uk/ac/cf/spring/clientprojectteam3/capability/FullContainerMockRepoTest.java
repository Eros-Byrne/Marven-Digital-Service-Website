package uk.ac.cf.spring.clientprojectteam3.capability;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.ac.cf.spring.clientprojectteam3.Capabilities.Capability;
import uk.ac.cf.spring.clientprojectteam3.Capabilities.CapabilityRepository;
import uk.ac.cf.spring.clientprojectteam3.Capabilities.Difficulty;
import uk.ac.cf.spring.clientprojectteam3.Capabilities.Resource;
import uk.ac.cf.spring.clientprojectteam3.Skills.Skill;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FullContainerMockRepoTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CapabilityRepository capRepo;

    @Test
    public void tableShouldNotBeRenderedIfNoResources() throws Exception {
        when(capRepo.getCapability(2L)).thenReturn(Optional.of(new Capability(
                2L, "Title", "Description")));

        when(capRepo.getResourcesForACapability(2L)).thenReturn(List.of());

        when(capRepo.getSkillsForACapability(2L)).thenReturn(List.of(
                new Skill(1L, "Skill1"),
                new Skill(2L, "Skill2")
        ));

        MvcResult result = mvc.
                perform(get("/capability/2"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertFalse(content.contains("<table id=\"resources-table\" class=\"table-bordered rounded w-100\">"));
    }

    @Test
    public void tableShouldNotBeRenderedIfNoSkills() throws Exception {
        when(capRepo.getCapability(2L)).thenReturn(Optional.of(new Capability(
                2L, "Title", "Description")));

        when(capRepo.getResourcesForACapability(2L)).thenReturn(List.of(
                new Resource(1L, "Resource1", Difficulty.Low),
                new Resource(2L, "Resource1", Difficulty.Medium)
        ));

        when(capRepo.getSkillsForACapability(2L)).thenReturn(List.of());

        MvcResult result = mvc.
                perform(get("/capability/2"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertFalse(content.contains("<div id=\"skills-card\" class=\"col-md-3 ms-auto card p-0\">"));
    }
}

package uk.ac.cf.spring.clientprojectteam3.capability;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.ac.cf.spring.clientprojectteam3.Capabilities.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private CapabilityRepository capRepo;

    @InjectMocks
    private CapabilityServiceImpl capService;


    @Test
    public void shouldReturnCapability() {

        Capability capability = new Capability(1L, "Mock Title", "Mock Description");
        when(capRepo.getCapability(1L)).thenReturn(Optional.of(capability));

        Capability result = capService.getCapability(1L);

        assertEquals("Mock Title", result.getTitle());
        assertEquals("Mock Description", result.getDescription());
    }

    @Test
    public void shouldReturnResources() {

        List<Resource> resources = List.of(
                new Resource(1L, "Content1", Difficulty.Low),
                new Resource(2L, "Content2", Difficulty.High));

        when(capRepo.getResourcesForACapability(1L)).thenReturn(resources);
        List<Resource> result = capService.getResources(1L);

        assertEquals(2, result.size());
        assertEquals("Content1", result.getFirst().getContent());
        assertEquals(Difficulty.High, result.get(1).getDifficulty());
    }

    @Test
    public void shouldReturnSkills() {
        List<Skill> skills = List.of(
                new Skill(1L, "Skill1"),
                new Skill(2L, "Skill2"));

        when(capRepo.getSkillsForACapability(1L)).thenReturn(skills);
        List<Skill> result = capService.getSkills(1L);

        assertEquals(2, result.size());
        assertEquals("Skill1", result.getFirst().getName());
        assertEquals("Skill2", result.get(1).getName());
    }

    @Test
    public void shouldReturnEmptyResources() {
        when(capRepo.getResourcesForACapability(1L)).thenReturn(List.of());

        List<Resource> result = capService.getResources(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptySkills() {
        when(capRepo.getSkillsForACapability(1L)).thenReturn(List.of());

        List<Skill> result = capService.getSkills(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenCapabilityNotFound() {
        when(capRepo.getCapability(12345L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> capService.getCapability(12345L));
    }

    //Exception when id is null
    @Test
    public void shouldThrowExceptionIfIdIsNull() {

        assertThrows(IllegalArgumentException.class,
                () -> capService.getCapability(null));
    }
}

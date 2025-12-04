package uk.ac.cf.spring.clientprojectteam3.capability;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.cf.spring.clientprojectteam3.capabilities.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
@Import(CapabilityRepositoryImpl.class)
@Transactional
public class RepositoryTest {

    @Autowired
    private CapabilityRepository capRepo;

    @Test
    public void shouldReturnCapability() {

        Optional<Capability> optionalCapability = capRepo.getCapability(1L);

        Capability cap = optionalCapability.get();

        assertNotNull(cap);
        assertEquals("Normal Capability1", cap.getTitle());
    }

    @Test
    public void shouldReturnAllResourcesApplicable() {

        List<Resource> resources = capRepo.getResourcesForACapability(1L);
        assertEquals(1, resources.size());
        assertEquals("Resource 1", resources.getFirst().getContent());
    }

    @Test
    public void shouldReturnAllSkillsApplicable() {

        List<Skill> skills = capRepo.getSkillsForACapability(1L);
        assertEquals(2, skills.size());
        assertEquals("Skill 1", skills.getFirst().getName());
        assertEquals("Skill 2", skills.get(1).getName());
    }

    @Test
    public void shouldReturnEmptyCapabilityIfIDDoesNotExist() {

        Optional<Capability> optionalCapability = capRepo.getCapability(12345L);
        assertTrue(optionalCapability.isEmpty());
    }

    @Test
    public void shouldReturnEmptyResourceIfResourceDoesNotExist() {

        List<Resource> resources = capRepo.getResourcesForACapability(12345L);
        assertTrue(resources.isEmpty());
    }

    @Test
    public void shouldReturnEmptySkillsIfIDDoesNotExist() {

        List<Skill> skills = capRepo.getSkillsForACapability(12345L);
        assertTrue(skills.isEmpty());
    }


}

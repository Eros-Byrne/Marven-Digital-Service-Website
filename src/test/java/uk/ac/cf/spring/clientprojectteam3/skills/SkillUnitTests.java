package uk.ac.cf.spring.clientprojectteam3.skills;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.ac.cf.spring.clientprojectteam3.Skills.Skill;
import uk.ac.cf.spring.clientprojectteam3.Skills.SkillRepositoryImpl;
import uk.ac.cf.spring.clientprojectteam3.Skills.SkillServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SkillUnitTests {

    @Test
    public void shouldReturnListOfSkills() {

        SkillRepositoryImpl mockRepository = Mockito.mock(SkillRepositoryImpl.class);

        Skill skill11 = new Skill();
        Skill skill12 = new Skill();
        skill11.setName("Skill 1");
        skill12.setName("Skill 2");

        Mockito.when(mockRepository.getSkills()).thenReturn(List.of(skill11, skill12));

        SkillServiceImpl service = new SkillServiceImpl(mockRepository);

        List<Skill> skills = service.getSkills();

        assertFalse(skills.isEmpty());
        assertEquals(2, skills.size());
        assertEquals("Skill 1", skills.get(0).getName());
    }
}

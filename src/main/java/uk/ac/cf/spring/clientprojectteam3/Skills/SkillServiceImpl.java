package uk.ac.cf.spring.clientprojectteam3.Skills;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository aSkillRepository) {
        this.skillRepository = aSkillRepository;
    }

    public List<Skill> getSkills() {
      return skillRepository.getSkills();
    }

}

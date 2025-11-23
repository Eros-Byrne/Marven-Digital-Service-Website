package uk.ac.cf.spring.clientprojectteam3.Capabilities;

import org.springframework.stereotype.Service;
import uk.ac.cf.spring.clientprojectteam3.Skills.Skill;

import java.util.List;

@Service
public class CapabilityServiceImpl implements CapabilityService {

    private final CapabilityRepository capRepository;

    public CapabilityServiceImpl(CapabilityRepository aCapRepository) {
        this.capRepository = aCapRepository;
    }


    public Capability getCapability(Long id) {
        return capRepository.getCapability(id);
    }

    public List<Resource> getResources(Long id) {
        return capRepository.getResourcesForACapability(id);
    }

    public List<Skill> getSkills(Long id) {
        return capRepository.getSkillsForACapability(id);
    }
}

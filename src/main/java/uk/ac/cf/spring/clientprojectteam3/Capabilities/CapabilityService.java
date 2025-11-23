package uk.ac.cf.spring.clientprojectteam3.Capabilities;

import uk.ac.cf.spring.clientprojectteam3.Skills.Skill;

import java.util.List;

public interface CapabilityService {

    Capability getCapability(Long id);
    List<Resource> getResources(Long id);
    List<Skill> getSkills(Long id);
}

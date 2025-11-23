package uk.ac.cf.spring.clientprojectteam3.Capabilities;

import uk.ac.cf.spring.clientprojectteam3.Skills.Skill;

import java.util.List;

public interface CapabilityRepository {

    Capability getCapability(Long id);
    List<Resource> getResourcesForACapability(Long id);
    List<Skill> getSkillsForACapability(Long id);
}

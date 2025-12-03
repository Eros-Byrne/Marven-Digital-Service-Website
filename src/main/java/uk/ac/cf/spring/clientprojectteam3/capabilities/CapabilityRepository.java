package uk.ac.cf.spring.clientprojectteam3.capabilities;


import java.util.List;
import java.util.Optional;

public interface CapabilityRepository {

    Optional<Capability> getCapability(Long id);
    List<Resource> getResourcesForACapability(Long id);
    List<Skill> getSkillsForACapability(Long id);
    List<Outcome> findAllOutcomes();
    Outcome findOutcomeById(Long id);
    List<Capability> findAllCapabilitiesForAnOutcome(Long outcomeId);
}

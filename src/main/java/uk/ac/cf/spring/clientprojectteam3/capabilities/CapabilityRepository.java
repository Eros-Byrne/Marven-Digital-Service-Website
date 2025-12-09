package uk.ac.cf.spring.clientprojectteam3.capabilities;


import uk.ac.cf.spring.clientprojectteam3.admin.outcome.AdminOutcome;

import java.util.List;
import java.util.Optional;

public interface CapabilityRepository {

    // CAPABILITIES LOGIC
    Optional<Capability> getCapability(Long id);
    List<Resource> getResourcesForACapability(Long id);
    List<Skill> getSkillsForACapability(Long id);

    // OUTCOMES LOGIC
    List<Outcome> findAllOutcomes();
    Outcome findOutcomeById(Long id);
    List<Capability> findAllCapabilitiesForAnOutcome(Long outcomeId);

    // ADMIN OUTCOMES LOGIC
    List<AdminOutcome> findAllOutcomesWithNumberOfCapabilities();
    void CreateOutcome(String title);
    void deleteOutcome(Long id);
}

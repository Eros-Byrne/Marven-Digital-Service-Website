package uk.ac.cf.spring.clientprojectteam3.capabilities;


import uk.ac.cf.spring.clientprojectteam3.admin.capability.AdminCapability;
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

    AdminOutcome findAdminOutcomeById(Long id);
    void updateOutcome(Long id, String title);

    // ADMIN CAPABILITIES LOGIC
    List<AdminCapability> findCapabilitiesForOutcome(Long outcomeId);
    void createCapability(Long outcomeId, String title, String description);
    void deleteCapability(Long capabilityId);
    AdminCapability findCapabilityById(Long capabilityId);
    void updateCapability(Long capabilityId, String title, String description);

    List<Capability> getAllCapabilitiesByOutcomeId(Long id);

    Long getOutcomeIdByQuizId(int id);
}

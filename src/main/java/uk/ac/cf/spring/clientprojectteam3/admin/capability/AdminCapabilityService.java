package uk.ac.cf.spring.clientprojectteam3.admin.capability;

import java.util.List;
import java.util.Optional;

public interface AdminCapabilityService {

    List<AdminCapability> getCapabilitiesForOutcome(Long outcomeId);

    AdminCapability getCapability(Long capabilityId);

    void createCapability(Long outcomeId, String title, String description);

    void updateCapability(Long capabilityId, String title, String description);

    void deleteCapability(Long capabilityId);
}

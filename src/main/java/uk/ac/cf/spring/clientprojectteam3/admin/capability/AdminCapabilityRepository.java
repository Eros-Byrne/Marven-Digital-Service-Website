package uk.ac.cf.spring.clientprojectteam3.admin.capability;

import java.util.List;
import java.util.Optional;

public interface AdminCapabilityRepository {

    List<AdminCapability> findByOutcomeId(Long outcomeId);

    Optional<AdminCapability> findById(Long capabilityId);

    void create(Long outcomeId, String title, String description);

    void update(Long capabilityId, String title, String description);

    void delete(Long capabilityId);
}

package uk.ac.cf.spring.clientprojectteam3.admin.capability;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCapabilityServiceImpl implements AdminCapabilityService {

    private final AdminCapabilityRepository repository;

    @Override
    public List<AdminCapability> getCapabilitiesForOutcome(Long outcomeId) {
        return repository.findByOutcomeId(outcomeId);
    }

    @Override
    public AdminCapability getCapability(Long capabilityId) {
        return repository.findById(capabilityId)
                .orElseThrow(() -> new IllegalArgumentException("Capability not found"));
    }

    @Override
    public void createCapability(Long outcomeId, String title, String description) {
        repository.create(outcomeId, title, description);
    }

    @Override
    public void updateCapability(Long capabilityId, String title, String description) {
        repository.update(capabilityId, title, description);
    }

    @Override
    public void deleteCapability(Long capabilityId) {
        repository.delete(capabilityId);
    }
}

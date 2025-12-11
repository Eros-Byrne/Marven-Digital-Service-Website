package uk.ac.cf.spring.clientprojectteam3.admin.outcome;

import org.springframework.stereotype.Service;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityRepository;

import java.util.List;

@Service
public class AdminOutcomeServiceImpl implements AdminOutcomeService {

    private final CapabilityRepository capRepo;

    public AdminOutcomeServiceImpl(CapabilityRepository capabilityRepository) {
        this.capRepo = capabilityRepository;
    }
    @Override
    public List<AdminOutcome> getAllOutcomesWithCapabilityCount() {
        return capRepo.findAllOutcomesWithNumberOfCapabilities();
    }
    @Override
    public void createOutcome(String outcomeTitle) {
        capRepo.CreateOutcome(outcomeTitle);
    }
    @Override
    public void deleteOutcome(Long outcomeId) {
        capRepo.deleteOutcome(outcomeId);
    }
    @Override
    public void updateOutcome(Long id, String title) {
        capRepo.updateOutcome(id, title);
    }
    @Override
    public AdminOutcome getOutcomeById(Long id) {
        return capRepo.findAllOutcomesWithNumberOfCapabilities()
                .stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Outcome not found"));
    }
}

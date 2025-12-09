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

    public List<AdminOutcome> getAllOutcomesWithCapabilityCount() {
        return capRepo.findAllOutcomesWithNumberOfCapabilities();
    }

    public void createOutcome(String outcomeTitle) {
        capRepo.CreateOutcome(outcomeTitle);
    }

    public void deleteOutcome(Long outcomeId) {
        capRepo.deleteOutcome(outcomeId);
    }
}

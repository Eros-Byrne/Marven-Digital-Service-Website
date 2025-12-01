package uk.ac.cf.spring.clientprojectteam3.Capabilities;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CapabilityServiceImpl implements CapabilityService {

    private final CapabilityRepository capRepository;

    public CapabilityServiceImpl(CapabilityRepository aCapRepository) {
        this.capRepository = aCapRepository;
    }


    public Capability getCapability(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

        Optional<Capability> capability = capRepository.getCapability(id);

        if (capability.isPresent()) {
            return capability.get();
        } else {
            throw new IllegalArgumentException("Capability not found");
        }
    }

    public List<Resource> getResources(Long id) {
        return capRepository.getResourcesForACapability(id);
    }

    public List<Skill> getSkills(Long id) {
        return capRepository.getSkillsForACapability(id);
    }

    public List<Outcome> getAllOutcomes() {
        return capRepository.findAllOutcomes();
    }

    public Outcome getOutcomeById(Long id) {
        return capRepository.findOutcomeById(id);
    }

    public void setCapabilitiesForOutcome(Outcome outcome) {
        List<Capability> capabilities = capRepository.findAllCapabilitiesForAnOutcome(outcome.getId());
        outcome.setCapabilities(capabilities);
    }
}

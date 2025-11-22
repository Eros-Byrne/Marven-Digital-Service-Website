package uk.ac.cf.spring.clientprojectteam3.Capabilities;

import org.springframework.stereotype.Service;

@Service
public class CapabilityServiceImpl implements CapabilityService {

    private final CapabilityRepository capRepository;

    public CapabilityServiceImpl(CapabilityRepository aCapRepository) {
        this.capRepository = aCapRepository;
    }


    public Capability getCapability(Long id) {
        return capRepository.getCapability(id);
    }
}

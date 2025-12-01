package uk.ac.cf.spring.clientprojectteam3.Capabilities;


import java.util.List;

public interface CapabilityService {

    Capability getCapability(Long id);
    List<Resource> getResources(Long id);
    List<Skill> getSkills(Long id);
    List<Outcome> getAllOutcomes();
    Outcome getOutcomeById(Long id);
    void setCapabilitiesForOutcome(Outcome outcome);
}

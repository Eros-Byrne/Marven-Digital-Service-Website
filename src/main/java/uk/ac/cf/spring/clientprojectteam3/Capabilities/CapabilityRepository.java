package uk.ac.cf.spring.clientprojectteam3.Capabilities;

import java.util.List;

public interface CapabilityRepository {

    Capability getCapability(Long id);
    List<Resource> getResourcesForACapability(Long id);
}

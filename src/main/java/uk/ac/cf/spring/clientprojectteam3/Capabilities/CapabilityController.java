package uk.ac.cf.spring.clientprojectteam3.Capabilities;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CapabilityController {

    private final CapabilityService capabilityService;

    public CapabilityController(CapabilityService aCapabilityService) {
        this.capabilityService = aCapabilityService;
    }

    @GetMapping("/capability/{id}")
    public ModelAndView getCapability(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("capabilities/single_capability");
        Capability capability = capabilityService.getCapability(id);
        mv.addObject("capability", capability);
        List<Resource> resources = capabilityService.getResources(id);
        mv.addObject("resources", resources);
        return mv;
    }

}

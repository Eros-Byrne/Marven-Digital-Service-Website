package uk.ac.cf.spring.clientprojectteam3.admin.capability;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/capabilities")
public class AdminCapabilityController {

    private final AdminCapabilityService adminCapabilityService;

    public AdminCapabilityController(AdminCapabilityService adminCapabilityService) {
        this.adminCapabilityService = adminCapabilityService;
    }

    @GetMapping("/{outcomeId}")
    public ModelAndView listCapabilities(@PathVariable Long outcomeId) {
        ModelAndView mv = new ModelAndView("admin/capabilities-list");
        mv.addObject(
                "capabilities",
                adminCapabilityService.getCapabilitiesForOutcome(outcomeId)
        );
        mv.addObject("outcomeId", outcomeId);
        return mv;
    }

    @GetMapping("/{outcomeId}/add")
    public ModelAndView addCapabilityForm(@PathVariable Long outcomeId) {
        ModelAndView mv = new ModelAndView("admin/create-capability");
        mv.addObject("outcomeId", outcomeId);
        return mv;
    }

    @PostMapping("/{outcomeId}/add")
    public ModelAndView addCapability(
            @PathVariable Long outcomeId,
            @RequestParam String title,
            @RequestParam String description
    ) {
        adminCapabilityService.createCapability(outcomeId, title, description);
        return new ModelAndView("redirect:/admin/capabilities/" + outcomeId);
    }

    @PostMapping("/delete/{capabilityId}")
    public ModelAndView deleteCapability(@PathVariable Long capabilityId) {
        adminCapabilityService.deleteCapability(capabilityId);
        return new ModelAndView("redirect:/admin/outcomes");
    }
    @GetMapping("/edit/{capabilityId}")
    public ModelAndView editCapabilityForm(@PathVariable Long capabilityId) {
        ModelAndView mv = new ModelAndView("admin/edit-capability");
        mv.addObject("capability", adminCapabilityService.getCapability(capabilityId));
        return mv;
    }

    @PostMapping("/edit")
    public ModelAndView saveCapabilityEdit(
            @RequestParam Long id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Long outcomeId
    ) {
        adminCapabilityService.updateCapability(id, title, description);
        return new ModelAndView("redirect:/admin/capabilities/" + outcomeId);
    }

}

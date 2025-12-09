package uk.ac.cf.spring.clientprojectteam3.admin.outcome;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminOutcomeController {

    private final AdminOutcomeService adminOutcomeService;

    public AdminOutcomeController(AdminOutcomeService aAdminOutcomeService) {
        adminOutcomeService = aAdminOutcomeService;
    }

    @GetMapping("/outcomes")
    public ModelAndView ListOfOutcomes() {
        ModelAndView mv = new ModelAndView("admin/outcomes-list");
        List<AdminOutcome> outcomes = adminOutcomeService.getAllOutcomesWithCapabilityCount();
        mv.addObject("outcomes", outcomes);
        return mv;
    }

    @GetMapping("/outcomes/add")
    public ModelAndView AddOutcomeForm() {
        ModelAndView mv = new ModelAndView("admin/create-outcome");
        return mv;
    }

    @PostMapping("/outcomes/add")
    public ModelAndView AddNewOutcome(@ModelAttribute("title") String title) {
        ModelAndView mv;
        adminOutcomeService.createOutcome(title);
        mv = new ModelAndView("redirect:/admin/outcomes");
        return mv;
    }

    @PostMapping("/outcome/delete/{id}")
    public ModelAndView deleteOutcome(@PathVariable("id") int id) {
        ModelAndView mv;
        adminOutcomeService.deleteOutcome((long) id);
        mv = new ModelAndView("redirect:/admin/outcomes");
        return mv;
    }
}

package uk.ac.cf.spring.clientprojectteam3.admin.outcome;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ModelAndView AddNewOutcome() {
        ModelAndView mv = new ModelAndView("admin/create-outcome");
        return mv;
    }
}

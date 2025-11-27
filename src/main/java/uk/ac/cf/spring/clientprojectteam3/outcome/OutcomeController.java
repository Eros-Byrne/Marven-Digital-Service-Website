package uk.ac.cf.spring.clientprojectteam3.outcome;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/outcomes")
public class OutcomeController {

    private final OutcomeService outcomeService;

    public OutcomeController(OutcomeService outcomeService) {
        this.outcomeService = outcomeService;
    }

    @GetMapping
    public String showOutcomes(Model model) {
        List<Outcome> outcomes = outcomeService.getAllOutcomes();
        model.addAttribute("outcomes", outcomes);
        return "outcomes";
    }
}
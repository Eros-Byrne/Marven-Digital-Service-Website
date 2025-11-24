package uk.ac.cf.spring.clientprojectteam3.Skills;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService aSkillService) {
        this.skillService = aSkillService;
    }

    @GetMapping("/skills")
    public ModelAndView getSkills() {
        ModelAndView mv = new ModelAndView("skills/skills_list");
        List<Skill> skills = skillService.getSkills();
        mv.addObject("skills", skills);
        return mv;
    }
}

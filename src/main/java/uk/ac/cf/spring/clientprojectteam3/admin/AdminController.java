package uk.ac.cf.spring.clientprojectteam3.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public ModelAndView admin() {
        return new ModelAndView("admin/dashboard");
    }
}

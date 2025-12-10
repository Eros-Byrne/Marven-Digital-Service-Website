package uk.ac.cf.spring.clientprojectteam3.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomePageController {

    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }
}

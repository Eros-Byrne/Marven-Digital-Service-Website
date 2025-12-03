package uk.ac.cf.spring.clientprojectteam3.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        Model model) {

        if (error != null) {
            model.addAttribute("error", "Invalid email or password.");
        }
        return "login";
    }
}

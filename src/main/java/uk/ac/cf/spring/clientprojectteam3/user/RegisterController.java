package uk.ac.cf.spring.clientprojectteam3.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        return "login/register";
    }

    @PostMapping("/register")
    public String submit(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "login/register";
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("emailError", "Email is already registered");
            return "login/register";
        }

        userService.registerUser(user);
        return "redirect:/login?registered";
    }
}

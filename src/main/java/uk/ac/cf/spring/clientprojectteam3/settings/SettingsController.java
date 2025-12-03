package uk.ac.cf.spring.clientprojectteam3.settings;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uk.ac.cf.spring.clientprojectteam3.user.User;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/settings")
public class SettingsController {

    private final UserService userService;

    @GetMapping
    public String settings(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        return "settings";
    }

    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 Model model) {

        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);

        if (!userService.checkPassword(user, currentPassword)) {
            model.addAttribute("passwordError", "Current password is incorrect.");
            return "settings";
        }

        userService.updatePassword(user.getUserid(), newPassword);
        model.addAttribute("passwordSuccess", "Password updated successfully!");
        return "settings";
    }

    @PostMapping("/change-email")
    public String changeEmail(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam String newEmail,
                              Model model) {

        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);

        if (userService.findByEmail(newEmail) != null) {
            model.addAttribute("emailError", "Email already in use.");
            return "settings";
        }

        userService.updateEmail(user.getUserid(), newEmail);
        model.addAttribute("emailSuccess", "Email updated successfully!");
        return "settings";
    }

    @PostMapping("/change-name")
    public String changeName(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @NotBlank @Size(min = 2) String newName,
            Model model) {

        User user = userService.findByEmail(userDetails.getUsername());
        userService.updateName(user.getUserid(), newName);

        model.addAttribute("user", user);
        model.addAttribute("nameSuccess", "Name updated successfully");
        return "settings";
    }
    @PostMapping("/change-phone")
    public String changePhone(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam String newPhone,
                              Model model) {

        User user = userService.findByEmail(userDetails.getUsername());
        userService.updatePhone(user.getUserid(), newPhone);

        model.addAttribute("user", user);
        model.addAttribute("phoneSuccess", "Phone number updated successfully!");
        return "settings";
    }

}

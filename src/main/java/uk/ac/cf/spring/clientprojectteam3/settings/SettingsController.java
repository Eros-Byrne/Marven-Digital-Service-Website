package uk.ac.cf.spring.clientprojectteam3.settings;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.cf.spring.clientprojectteam3.security.CustomUserDetails;
import uk.ac.cf.spring.clientprojectteam3.user.User;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;

@Controller
@RequestMapping("/settings")
@RequiredArgsConstructor
@Validated
public class SettingsController {

    private final UserService userService;

    @GetMapping
    public String settings(@AuthenticationPrincipal UserDetails userDetails,
                           org.springframework.ui.Model model) {

        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        return "settings";
    }


    @PostMapping("/change-name")
    public String changeName(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam
            @NotBlank
            @Size(min = 2, max = 100)
            @Pattern(
                    regexp = "^[A-Za-z ]+$",
                    message = "Name must only contain letters and spaces"
            )
            String newName,
            RedirectAttributes ra) {

        User user = userService.findByEmail(principal.getUsername());
        userService.updateName(user.getUserid(), newName);

        CustomUserDetails updatedDetails =
                new CustomUserDetails(
                        principal.getUsername(),
                        principal.getPassword(),
                        newName,
                        principal.getAuthorities()
                );

        Authentication newAuth =
                new UsernamePasswordAuthenticationToken(
                        updatedDetails,
                        principal.getPassword(),
                        principal.getAuthorities()
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(newAuth);

        ra.addFlashAttribute("emailSuccess", "Name updated successfully.");
        return "redirect:/settings";
    }

    @PostMapping("/change-email")
    public String changeEmail(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam
            @NotBlank
            @jakarta.validation.constraints.Email(message = "Enter a valid email address")
            String newEmail,
            RedirectAttributes ra) {

        User user = userService.findByEmail(userDetails.getUsername());

        User existing = userService.findByEmail(newEmail);
        if (existing != null && !existing.getUserid().equals(user.getUserid())) {
            ra.addFlashAttribute("error", "Email already in use.");
            return "redirect:/settings";
        }

        userService.updateEmail(user.getUserid(), newEmail);
        ra.addFlashAttribute("emailSuccess", "Email updated successfully.");

        return "redirect:/settings";
    }

    @PostMapping("/change-phone")
    public String changePhone(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam
            @Pattern(
                    regexp = "^(07\\d{9}|\\+44\\s?7\\d{9})$",
                    message = "Enter a valid UK phone number"
            )
            String newPhone,
            RedirectAttributes ra) {

        User user = userService.findByEmail(userDetails.getUsername());
        userService.updatePhone(user.getUserid(), newPhone);

        ra.addFlashAttribute("phoneSuccess", "Phone number updated successfully.");
        return "redirect:/settings";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String currentPassword,
            @RequestParam
            @NotBlank(message = "New password is required")
            @Size(min = 8, message = "New password must be at least 8 characters")
            String newPassword,
            RedirectAttributes ra) {

        User user = userService.findByEmail(userDetails.getUsername());


        if (!userService.checkPassword(user, currentPassword)) {
            ra.addFlashAttribute(
                    "passwordError",
                    "Current password is incorrect."
            );
            return "redirect:/settings";
        }

        userService.updatePassword(user.getUserid(), newPassword);
        return "redirect:/login?passwordChanged";
    }
}

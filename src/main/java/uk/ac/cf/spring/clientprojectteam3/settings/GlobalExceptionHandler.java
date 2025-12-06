package uk.ac.cf.spring.clientprojectteam3.config;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolation(
            ConstraintViolationException ex,
            RedirectAttributes ra) {

        String message = ex.getConstraintViolations()
                .iterator()
                .next()
                .getMessage();

        ra.addFlashAttribute("error", message);
        return "redirect:/settings";
    }
}

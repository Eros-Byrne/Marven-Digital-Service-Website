package uk.ac.cf.spring.clientprojectteam3.Capabilities;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CapabilityExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ModelAndView handleTypeMismatch(MethodArgumentTypeMismatchException e,
                                           HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.startsWith("/capabilities")) {
            return new ModelAndView("redirect:/outcomes");
        }

        return new ModelAndView(("error"));
    }
}

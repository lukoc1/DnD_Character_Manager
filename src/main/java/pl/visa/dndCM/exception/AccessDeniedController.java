package pl.visa.dndCM.exception;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

// the same error page as CustomExceptionHandler
//      but -> 403 error happens (caught by filter in SecurityConfig) before ControllerAdvice can deal with it
@Controller
public class AccessDeniedController {

    @GetMapping("/access-denied")
    public String showAccessDenied(Model model) {
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("errorMessage", "You don't have permission to access this page.");
        model.addAttribute("errorCode", ErrorCode.ONLY_ADMIN_ACCESS.name());

        return "error/error";
    }
}

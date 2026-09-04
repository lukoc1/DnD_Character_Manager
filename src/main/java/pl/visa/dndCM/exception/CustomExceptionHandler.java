package pl.visa.dndCM.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.visa.dndCM.user.RegisterUserDTO;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleEntityNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", ex.getErrorCode().name());

        return "error/error";
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public String handleUserAlreadyExist(UserAlreadyExistException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("registerUserDTO", new RegisterUserDTO());

        return "user/register-user";
    }
}

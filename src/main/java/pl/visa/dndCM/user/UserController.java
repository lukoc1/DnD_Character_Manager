package pl.visa.dndCM.user;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showSighUpForm(Model model) {
        model.addAttribute("registerUserDTO", new RegisterUserDTO());
        return "user/register-user";
    }

    @PostMapping(value = "/register", params = "cancel")
    public String cancelUserRegistration() {
        return "redirect:/";
    }

    @PostMapping(value = "/register", params = "register")
    public String addUser(@Valid RegisterUserDTO userDTO, BindingResult result, Model model) {

        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .collect(Collectors.joining("\n"));
            model.addAttribute("errorMessage", errorMessage);
            return "user/register-user";
        }

        userService.save(userDTO);

        return "redirect:/";
    }

}

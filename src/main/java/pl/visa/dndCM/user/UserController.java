package pl.visa.dndCM.user;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/add")
    public String showSighUpForm(Model model) {
        model.addAttribute("registerUserDTO", new RegisterUserDTO());
        return "user/add-user";
    }

    @PostMapping(value = "/add", params = "cancel")
    public String cancelUserRegistration() {
        return "redirect:/";
    }

    @PostMapping(value = "/add", params = "register")
    public String addUser(@Valid RegisterUserDTO userDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/add-user";
        }

        try {
            userService.save(userDTO);
        } catch (IllegalArgumentException ex) {
            return "user/add-user";
        }
        return "redirect:/";
    }

}

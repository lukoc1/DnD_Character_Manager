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

    @GetMapping("/adduser")
    public String showSighUpForm(Model model) {
        model.addAttribute("registerUserDTO", new RegisterUserDTO());
        return "add-user";
    }

    @PostMapping(value = "/adduser", params = "cancel")
    public String cancelUserRegistration() {
        return "redirect:/";
    }

    @PostMapping(value = "/adduser", params = "register")
    public String addUser(@Valid RegisterUserDTO userDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-user";
        }

        try {
            userService.save(userDTO);
        } catch (IllegalArgumentException ex) {
            return "add-user";
        }
        return "redirect:/";
    }

//    @GetMapping("/login")
//    public String showHomePage(Model model) {
//        return "login";
//    }

//    @GetMapping("/list")
//    public String list(Model model) {
//
////        model.addAttribute("users", userService.fin)
//    }
}

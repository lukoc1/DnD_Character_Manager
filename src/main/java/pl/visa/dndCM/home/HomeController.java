package pl.visa.dndCM.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.visa.dndCM.avatar.AvatarService;
import pl.visa.dndCM.user.PasswordUtil;
import pl.visa.dndCM.user.RegisterUserDTO;
import pl.visa.dndCM.user.User;
import pl.visa.dndCM.user.UserService;

import java.util.Optional;

@Controller
public class HomeController {

    private final UserService userService;
    private final AvatarService avatarService;

    public HomeController(UserService userService, AvatarService avatarService) {
        this.userService = userService;
        this.avatarService = avatarService;
    }

    @GetMapping("/")
    public String loginPage() {
        return "index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute RegisterUserDTO userDTO) {

        Optional<User> user = userService.findByName(userDTO.getName());

        if (user.isPresent() && PasswordUtil.checkPassword(userDTO.getPassword(), user.get())) {
            return "redirect:/home";
        }
        return "redirect:/";
    }


    @GetMapping("/home")
    public String homePage(Model model) {
//        model.addAttribute("users", userService.findAll());
        model.addAttribute("avatars", avatarService.findAll());
        return "home";
    }
}

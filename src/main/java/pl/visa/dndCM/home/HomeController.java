package pl.visa.dndCM.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.visa.dndCM.user.UserService;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String loginPage(Model model) {
        // Keep the login page rendering simple; do not query users here.
        return "index";
    }



    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "home";
    }
}

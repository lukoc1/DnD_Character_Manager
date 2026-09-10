package pl.visa.dndCM.home;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.visa.dndCM.avatar.AvatarService;
import pl.visa.dndCM.exception.ResourceNotFoundException;
import pl.visa.dndCM.user.User;
import pl.visa.dndCM.user.UserDTO;
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
    public String showLoginPage(Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        return "login";
    }

    @GetMapping("/login")
    public String showLogin(Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        return "login";
    }



    @GetMapping("/home")
    public String homePage(Model model, Authentication authentication, HttpServletRequest request) {

        String name = authentication.getName();

        Optional<UserDTO> userOptional = userService.findByNameOptional(name);

        // Dodane bo był przypadek że w sesji zalogowany
        // był user a ręcznie usunąłem tego usera
        // i byłem zablokowany na error page
        if (userOptional.isEmpty()) {
            request.getSession().invalidate();
            SecurityContextHolder.clearContext();
            return "redirect:/login";
        }

        UserDTO currentUser = userOptional.get();
        model.addAttribute("avatars", avatarService.findAllByUserId(currentUser.getId()));
        return "home";

    }
}

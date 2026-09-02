package pl.visa.dndCM.home;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.visa.dndCM.avatar.AvatarService;
import pl.visa.dndCM.user.User;
import pl.visa.dndCM.user.UserService;

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

        // Jeśli użytkownik jest już uwierzytelniony, przekieruj go na /home
        // Zapobiega to wyświetlaniu strony logowania zalogowanym użytkownikom przy wejściu na katalog główny
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        return "login";
    }

    @GetMapping("/login")
    public String showLogin(Authentication authentication) {
        // Jawny handler GET dla /login — potrzebny, bo formLogin().loginPage("/login")
        // wskazuje na niestandardowy widok logowania. Jeśli użytkownik jest już zalogowany,
        // przekierowujemy go na /home. W przeciwnym razie zwracamy widok logowania; POST /login
        // jest obsługiwany automatycznie przez Spring Security.
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        return "login";
    }

    @GetMapping("/home")
    public String homePage(Model model, Authentication authentication) {

        String name = authentication.getName();
        User currentUser = userService.findByName(name).get();

        model.addAttribute("avatars", avatarService.findAllByUserId(currentUser.getId()));
        return "home";
    }
}

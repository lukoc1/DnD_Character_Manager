package pl.visa.dndCM.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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

    @GetMapping("/edit")
    public String showEditForm(Model model, Authentication authentication) {
        UserDTO userDTO = userService.findByEmail(authentication.getName());

        model.addAttribute("editUserDTO", EditUserDTO.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .build());

        return "user/edit-user";
    }

    @PostMapping("/edit")
    public String editUser(@Valid EditUserDTO editUserDTO, BindingResult result, Model model,
                            Authentication authentication, HttpServletRequest request, HttpServletResponse response) {

        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .collect(Collectors.joining("\n"));
            model.addAttribute("errorMessage", errorMessage);
            return "user/edit-user";
        }

        userService.update(authentication.getName(), editUserDTO);

        // wylogowanie bo email / hasło mogło się zmienić
        new SecurityContextLogoutHandler().logout(request, response, authentication);

        return "redirect:/login?edited";
    }


    @PostMapping("/delete")
    public String deleteSelf(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {

        UserDTO userDTO = userService.findByEmail(authentication.getName());
        userService.deleteUserById(userDTO.getId());

        // konczy sesje i daje komunikat o usunieciu
        new SecurityContextLogoutHandler().logout(request, response, authentication);

        return "redirect:/login?deleted";

    }

}

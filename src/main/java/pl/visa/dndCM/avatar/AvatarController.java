package pl.visa.dndCM.avatar;

import jakarta.validation.Valid;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.visa.dndCM.gameData.dndClass.DndClassService;
import pl.visa.dndCM.user.User;
import pl.visa.dndCM.user.UserDTO;
import pl.visa.dndCM.user.UserRepository;
import pl.visa.dndCM.user.UserService;

@Controller
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;
    private final UserService userService;
    private final DndClassService dndClassService;

    public AvatarController(AvatarService avatarService, UserService userService, DndClassService dndClassService) {
        this.avatarService = avatarService;
        this.userService = userService;
        this.dndClassService = dndClassService;
    }

    @GetMapping("/add")
    public String showAddAvatarForm(Model model) {
        model.addAttribute("avatarDTO", new AvatarDTO());
        model.addAttribute("dndClasses", dndClassService.findAll());
        return "avatar/add-avatar";
    }

    @PostMapping(value = "/add", params = "cancel")
    public String cancelAddingNewAvatar() {
        return "redirect:/";
    }

    @PostMapping(value = "/add", params = "save")
    public String saveAvatar(@Valid AvatarDTO avatarDTO, BindingResult result, Model model, Authentication authentication, RedirectAttributes ra) {

        if (result.hasErrors()) {
            model.addAttribute("dndClasses", dndClassService.findAll());
            return "avatar/add-avatar";
        }

        // current user name
        String name = authentication.getName();

        try {
            avatarService.save(avatarDTO, name);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("dndClasses", dndClassService.findAll());
            return "avatar/add-avatar";
        }
//        ra.addFlashAttribute("message", "Avatar utworzony");
        return "redirect:/home";
    }


    @GetMapping("/select/{id}")
    public String selectAvatar(Model model, @PathVariable Long id, RedirectAttributes ra, Authentication authentication) {

        // current user name
        String name = authentication.getName();

        Long userId = userService.findByName(name).getId();
        Long avatarOwnerId = avatarService.getAvatarById(id).getUserId();

        if (!userId.equals(avatarOwnerId)) {
            ra.addFlashAttribute("message", "Nie posiadasz dostępu do postaci [id = " + id + "]!");
            return "redirect:/home";
        }

        model.addAttribute("avatar", avatarService.getAvatarById(id));

        return "avatar/selected-avatar";
    }

    @GetMapping("/select/{id}/showcard")
    public String showCard(Model model, @PathVariable Long id) {

        model.addAttribute("avatar", avatarService.getAvatarById(id));

        return "avatar/avatar-card";
    }

    @GetMapping("/delete/{id}")
    public String deleteAvatarById(@PathVariable Long id) {
        avatarService.deleteAvatarById(id);

        return "redirect:/home";
    }




}
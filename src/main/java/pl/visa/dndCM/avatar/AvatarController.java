package pl.visa.dndCM.avatar;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.visa.dndCM.user.UserService;

@Controller
@AllArgsConstructor
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;
    private final UserService userService;


    @GetMapping("/select/{id}")
    public String selectAvatar(Model model, @PathVariable Long id, RedirectAttributes ra, Authentication authentication) {

        Long userId = userService.findByName(authentication.getName()).getId();
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
        model.addAttribute("proficiencies", avatarService.getSkillProficiencyNames(id));
        model.addAttribute("savingThrowAbilities", avatarService.getSavingThrowAbilities(id));
        model.addAttribute("passivePerception", avatarService.getPassivePerception(id));

        return "avatar/avatar-sheet";
    }

    @GetMapping("/delete/{id}")
    public String deleteAvatarById(@PathVariable Long id) {
        avatarService.deleteAvatarById(id);

        return "redirect:/home";
    }

    @GetMapping("/{id}/hit-dice/spend")
    public String spendHitDie(@PathVariable Long id) {
        avatarService.spendHitDie(id);

        return "redirect:/avatar/select/" + id + "/showcard";
    }
}

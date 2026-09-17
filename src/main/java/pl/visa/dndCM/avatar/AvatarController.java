package pl.visa.dndCM.avatar;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.visa.dndCM.avatar.health.AvatarHealthService;
import pl.visa.dndCM.avatar.inventory.AvatarInventoryService;
import pl.visa.dndCM.user.UserService;

@Controller
@AllArgsConstructor
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;
    private final AvatarHealthService avatarHealthService;
    private final AvatarInventoryService avatarInventoryService;
    private final UserService userService;


    @GetMapping("/select/{id}")
    public String selectAvatar(Model model, @PathVariable Long id, RedirectAttributes ra, Authentication authentication) {

        Long userId = userService.findByEmail(authentication.getName()).getId();
        Long avatarOwnerId = avatarService.getAvatarById(id).getUserId();

        if (!userId.equals(avatarOwnerId)) {
            ra.addFlashAttribute("message", "You cannot access this character [id = " + id + "]!");
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
        model.addAttribute("allItemNames", avatarInventoryService.getAllEquipmentItemNames());

        return "avatar/avatar-sheet";
    }

    @GetMapping("/delete/{id}")
    public String deleteAvatarById(@PathVariable Long id) {
        avatarService.deleteAvatarById(id);

        return "redirect:/home";
    }

    @GetMapping("/{id}/hit-dice/spend")
    public String spendHitDie(@PathVariable Long id) {
        avatarHealthService.spendHitDie(id);

        return "redirect:/avatar/select/" + id + "/showcard";
    }

    @PostMapping("/{id}/edit")
    public String editAvatar(@PathVariable Long id,
                              @RequestParam(defaultValue = "0") int damage,
                              @RequestParam(defaultValue = "0") int heal,
                              @RequestParam(defaultValue = "0") int tempHP,
                              @RequestParam(defaultValue = "0") int goldChange,
                              @RequestParam(required = false) String addItem,
                              @RequestParam(required = false) String loseItem,
                              RedirectAttributes ra) {
        if (heal > 0) {
            avatarHealthService.heal(id, heal);
        }
        if (tempHP > 0) {
            avatarHealthService.addTempHp(id, tempHP);
        }
        if (damage > 0) {
            avatarHealthService.takeDamage(id, damage);
        }


        if (goldChange > 0) {
            avatarInventoryService.addCoins(id, goldChange);
        } else if (goldChange < 0 && !avatarInventoryService.loseCoins(id, -goldChange)) {
            ra.addFlashAttribute("message", "Not enough gold!");
        }


        if (!addItem.isEmpty()) {
            avatarInventoryService.addItem(id, addItem);
        }
        if (!loseItem.isEmpty()) {
            avatarInventoryService.loseItem(id, loseItem);
        }

        return "redirect:/avatar/select/" + id + "/showcard";
    }
}

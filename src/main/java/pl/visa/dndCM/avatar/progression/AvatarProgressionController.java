package pl.visa.dndCM.avatar.progression;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.visa.dndCM.avatar.AvatarDTO;
import pl.visa.dndCM.avatar.AvatarService;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclassService;

// post-creation progression: subclass choice, level-up, ability score improvement
@Controller
@AllArgsConstructor
@RequestMapping("/avatar")
public class AvatarProgressionController {

    private final AvatarService avatarService;
    private final AvatarProgressionService avatarProgressionService;
    private final DndSubclassService dndSubclassService;


    // SUBCLASS

    @GetMapping("/{id}/level-up/subclass")
    public String showSubclassStep(@PathVariable Long id, Model model) {

        AvatarDTO avatarDTO = avatarService.getAvatarById(id);
        model.addAttribute("avatar", avatarDTO);
        model.addAttribute("subclasses", dndSubclassService.findByClassId(avatarDTO.getDndClassId()));

        return "avatar/add-avatar/create-subclass";
    }

    @PostMapping("/{id}/level-up/subclass")
    public String saveSubclassStep(@PathVariable Long id, @RequestParam Long subclassId, Model model) {

        if (subclassId == null) {

            AvatarDTO avatarDTO = avatarService.getAvatarById(id);
            model.addAttribute("avatar", avatarDTO);
            model.addAttribute("subclasses", dndSubclassService.findByClassId(avatarDTO.getDndClassId()));
            model.addAttribute("error", "Pick a subclass.");

            return "avatar/add-avatar/create-subclass";

        }
        avatarProgressionService.chooseSubclass(id, subclassId);
        return "redirect:/avatar/select/" + id + "/showcard";

    }


    // LEVEL UP

    @GetMapping("/{id}/level-up")
    public String showLevelUpStep(@PathVariable Long id, Model model) {
        AvatarDTO avatarDTO = avatarService.getAvatarById(id);

        model.addAttribute("avatar", avatarDTO);

        // do sprawdzenia czy na następnym lvl nie ma zmiany ability scores
        model.addAttribute("grantsAsi", avatarProgressionService.grantsAbilityScoreImprovementAtLevel(id, avatarDTO.getLevel() + 1));
        return "avatar/add-avatar/level-up";
    }

    @PostMapping("/{id}/level-up")
    public String confirmLevelUp(@PathVariable Long id) {
        avatarProgressionService.levelUp(id);

        // dla każdej klasy na lvl 3
        if (avatarProgressionService.canGainSubclass(id)) {
            return "redirect:/avatar/" + id + "/level-up/subclass";
        }

        // na różnych levelach dla klas - ale nie na lvl 3
        if (avatarProgressionService.grantsAbilityScoreImprovementAtLevel(id, avatarService.getAvatarById(id).getLevel())) {
            return "redirect:/avatar/" + id + "/level-up/asi";
        }

        return "redirect:/avatar/select/" + id + "/showcard";
    }

    // asi - ability score improvement
    @GetMapping("/{id}/level-up/asi")
    public String showAsiStep(@PathVariable Long id, Model model) {
        model.addAttribute("avatar", avatarService.getAvatarById(id));
        return "avatar/add-avatar/create-asi";
    }

    @PostMapping("/{id}/level-up/asi")
    public String saveAsiStep(@PathVariable Long id,
                              @RequestParam String mode,
                              @RequestParam(required = false) String ability1,
                              @RequestParam(required = false) String ability2,
                              Model model) {

        boolean invalid = ability1 == null || ability1.isBlank()
                || ("double".equals(mode) && (ability2 == null || ability2.isBlank() || ability1.equals(ability2)));

        if (invalid) {
            model.addAttribute("avatar", avatarService.getAvatarById(id));
            model.addAttribute("error", "Pick two different abilities.");
            return "avatar/add-avatar/create-asi";
        }

        avatarProgressionService.applyAbilityScoreImprovement(id, mode, ability1, ability2);

        return "redirect:/avatar/select/" + id + "/showcard";
    }
}

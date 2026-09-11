package pl.visa.dndCM.avatar.creation;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.visa.dndCM.avatar.AvatarDTO;
import pl.visa.dndCM.avatar.AvatarService;
import pl.visa.dndCM.gameData.background.Background;
import pl.visa.dndCM.gameData.background.BackgroundService;
import pl.visa.dndCM.gameData.dndClass.DndClass;
import pl.visa.dndCM.gameData.dndClass.DndClassService;
import pl.visa.dndCM.gameData.specie.SpecieService;

import java.util.List;

/** Character-creation wizard: step by step the user fills the avatar and it moves to the next page. */
@Controller
@AllArgsConstructor
@RequestMapping("/avatar")
public class AvatarCreationController {

    private final AvatarService avatarService;
    private final DndClassService dndClassService;
    private final BackgroundService backgroundService;
    private final SpecieService specieService;


    // Step 1 - name, background, class, species

    @GetMapping("/create")
    public String showAddAvatarForm(Model model) {
        model.addAttribute("avatarDTO", new AvatarDTO());
        addFormOptions(model);
        return "avatar/add-avatar/create-avatar";
    }

    @PostMapping(value = "/create", params = "cancel")
    public String cancelAddingNewAvatar() {
        return "redirect:/";
    }

    @PostMapping(value = "/create", params = "save")
    public String saveAvatar(@Valid AvatarDTO avatarDTO, BindingResult result, Model model, Authentication authentication) {

        if (result.hasErrors()) {
            addFormOptions(model);
            return "avatar/add-avatar/create-avatar";
        }

        Long avatarId = avatarService.save(avatarDTO, authentication.getName());

        return "redirect:/avatar/" + avatarId + "/create/class";
    }

    private void addFormOptions(Model model) {
        model.addAttribute("dndClasses", dndClassService.findAll());
        model.addAttribute("backgrounds", backgroundService.findAll());
        model.addAttribute("species", specieService.findAll());
    }


    // Step 2 - what the class gives + skill and starting equipment choices

    @GetMapping("/{id}/create/class")
    public String showClassStep(@PathVariable Long id, Model model) {
        addClassStepAttributes(model, id);
        return "avatar/add-avatar/create-class";
    }

    @PostMapping("/{id}/create/class")
    public String saveClassStep(@PathVariable Long id,
                                @RequestParam(name = "skills", required = false) List<String> skills,
                                @RequestParam(name = "equipment", defaultValue = "A") String equipment,
                                Model model) {

        int required = dndClassService.findById(avatarService.getAvatarById(id).getDndClassId()).getSkillChoiceCount();
        int chosen = skills == null ? 0 : skills.size();

        if (chosen != required) {
            addClassStepAttributes(model, id);
            model.addAttribute("error", "Choose exactly " + required + " skills.");
            return "avatar/add-avatar/create-class";
        }

        avatarService.saveClassStep(id, skills, equipment);

        return "redirect:/avatar/" + id + "/create/abilities";
    }


    // Step 3 - roll and assign the six ability scores

    @GetMapping("/{id}/create/abilities")
    public String showAbilitiesStep(@PathVariable Long id, Model model) {
        model.addAttribute("avatar", avatarService.getAvatarById(id));
        return "avatar/add-avatar/create-abilities";
    }

    @PostMapping("/{id}/create/abilities")
    public String saveAbilitiesStep(@PathVariable Long id, @RequestParam int str, @RequestParam int dex,
                                    @RequestParam int con, @RequestParam(name = "int") int intel, @RequestParam int wis,
                                    @RequestParam int cha, Model model) {

        List<Integer> scores = List.of(str, dex, con, intel, wis, cha);
        if (scores.stream().anyMatch(v -> v < 3 || v > 18)) {
            model.addAttribute("avatar", avatarService.getAvatarById(id));
            model.addAttribute("error", "Each ability score must be between 3 and 18.");
            return "avatar/add-avatar/create-abilities";
        }

        avatarService.saveAbilitiesStep(id, str, dex, con, intel, wis, cha);

        return "redirect:/avatar/" + id + "/create/background";
    }

    private void addClassStepAttributes(Model model, Long avatarId) {
        AvatarDTO avatar = avatarService.getAvatarById(avatarId);
        DndClass dndClass = dndClassService.findById(avatar.getDndClassId());

        model.addAttribute("avatar", avatar);
        model.addAttribute("dndClass", dndClass);
        model.addAttribute("savingThrows", String.join(", ", dndClass.getSavingThrowAbilities()));
    }


    // Step 4 - what the background gives + ability score increase + equipment choice (last step)

    @GetMapping("/{id}/create/background")
    public String showBackgroundStep(@PathVariable Long id, Model model) {
        addBackgroundStepAttributes(model, id);
        return "avatar/add-avatar/create-background";
    }

    @PostMapping("/{id}/create/background")
    public String saveBackgroundStep(@PathVariable Long id,
                                     @RequestParam(name = "abilityMode", defaultValue = "split") String abilityMode,
                                     @RequestParam(name = "plus2", required = false) String plus2Ability,
                                     @RequestParam(name = "plus1", required = false) String plus1Ability,
                                     @RequestParam(name = "equipment", defaultValue = "A") String equipment,
                                     Model model) {

        Background background = backgroundService.findById(avatarService.getAvatarById(id).getBackgroundId());
        List<String> options = background.getAbilityScoreOptions();

        boolean validSplit = options.contains(plus2Ability) && options.contains(plus1Ability)
                && !plus2Ability.equals(plus1Ability);
        if (!"all".equals(abilityMode) && !validSplit) {
            addBackgroundStepAttributes(model, id);
            model.addAttribute("error", "Pick two different abilities from the list.");
            return "avatar/add-avatar/create-background";
        }

        avatarService.saveBackgroundStep(id, abilityMode, plus2Ability, plus1Ability, equipment);

        return "redirect:/avatar/select/" + id;
    }

    private void addBackgroundStepAttributes(Model model, Long avatarId) {
        AvatarDTO avatar = avatarService.getAvatarById(avatarId);
        Background background = backgroundService.findById(avatar.getBackgroundId());

        model.addAttribute("avatar", avatar);
        model.addAttribute("background", background);
    }
}

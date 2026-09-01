package pl.visa.dndCM.avatar;

import jakarta.validation.Valid;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;
    private final ResourcePatternResolver resourcePatternResolver;

    public AvatarController(AvatarService avatarService, ResourcePatternResolver resourcePatternResolver) {
        this.avatarService = avatarService;
        this.resourcePatternResolver = resourcePatternResolver;
    }

    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("avatar", new AvatarDTO());
        return "avatar/add-avatar";
    }

    @PostMapping("/new")
    public String saveAvatar(@Valid AvatarDTO avatarDTO, BindingResult result, RedirectAttributes ra) {

        if (result.hasErrors()) {
            return "avatar/add-avatar";
        }

        avatarService.save(avatarDTO);
//        ra.addFlashAttribute("message", "Avatar utworzony");
        return "redirect:/home";
    }
}
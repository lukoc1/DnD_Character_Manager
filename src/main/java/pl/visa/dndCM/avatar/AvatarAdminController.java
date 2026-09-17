package pl.visa.dndCM.avatar;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.visa.dndCM.avatar.creation.AvatarCreationService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/avatars")
public class AvatarAdminController {

    private final AvatarCreationService avatarCreationService;

    @GetMapping("/delete-drafts")
    public String deleteDrafts() {
        avatarCreationService.deleteAllDrafts();
        return "Deleted all draft avatars";
    }
}

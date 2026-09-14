package pl.visa.dndCM.avatar;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/avatars")
public class AvatarAdminController {

    private final AvatarService avatarService;

    @GetMapping("/delete-drafts")
    public String deleteDrafts() {
        avatarService.deleteAllDrafts();
        return "Deleted all draft avatars";
    }
}

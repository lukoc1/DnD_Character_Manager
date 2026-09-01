package pl.visa.dndCM.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import pl.visa.dndCM.avatar.Avatar;
import pl.visa.dndCM.avatar.AvatarDTO;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
//    private UUID uuid;
    @NotBlank
    private String name;
    private List<String> avatarList;
}

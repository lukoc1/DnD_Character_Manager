package pl.visa.dndCM.avatar;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.visa.dndCM.user.User;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvatarDTO {

    private Long id;
    private Long userId;

    private String user;

    @NotBlank
    @Size(min = 2, max = 30)
    private String name;
    private String background;
    private String className;
    private String species;
    private String subclassName;
}

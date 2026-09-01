package pl.visa.dndCM.avatar;

import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.user.User;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvatarDTO {

    private Long id;

    private String owner;

    private String name;
//    private String background;
//    private String className;
//    private String species;
//    private String subclassName;
}

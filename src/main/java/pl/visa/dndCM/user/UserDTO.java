package pl.visa.dndCM.user;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
//    private UUID uuid;

    private String firstName;
    private String lastName;
    private String email;

    private List<String> avatarList;
}

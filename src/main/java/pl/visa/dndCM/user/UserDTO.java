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

    private String firstName;
    private String lastName;
    private String email;
    private Role role;

    private List<String> avatarList;
}

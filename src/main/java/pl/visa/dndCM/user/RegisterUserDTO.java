package pl.visa.dndCM.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegisterUserDTO {

    private String name;
    private String password;
}

package pl.visa.dndCM.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegisterUserDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String password;
}

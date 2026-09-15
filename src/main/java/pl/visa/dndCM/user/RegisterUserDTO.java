package pl.visa.dndCM.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegisterUserDTO {

    @NotBlank
    @Length(min = 7, max = 30)
    private String password;

    @NotBlank
    @Length(min = 2, max = 30)
    private String firstName;
    @NotBlank
    @Length(min = 2, max = 30)
    private String lastName;

    @NotBlank
    @Email
    private String email;
}

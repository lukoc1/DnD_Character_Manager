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
public class EditUserDTO {

    @NotBlank
    @Length(min = 2, max = 30)
    private String firstName;
    @NotBlank
    @Length(min = 2, max = 30)
    private String lastName;

    @NotBlank
    @Email
    private String email;

    // opcjonalne, puste = nie zmieniaj hasła (walidacja długości w UserService.update)
    private String password;
}

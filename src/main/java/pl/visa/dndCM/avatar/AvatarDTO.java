package pl.visa.dndCM.avatar;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvatarDTO {

    private Long id;
    private Long userId;

    private String user;

    // wybrana klasa (id z formularza)
    @NotNull
    private Long dndClassId;

    @NotBlank
    @Size(min = 2, max = 30)
    private String name;
    private String background;

    // tylko do wyświetlania
    private String className;

    private String species;
    private String subclassName;
}

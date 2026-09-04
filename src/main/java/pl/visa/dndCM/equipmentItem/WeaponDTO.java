package pl.visa.dndCM.equipmentItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeaponDTO {

    private Long id;

    @NotBlank(message = "Nazwa przedmiotu nie może być pusta")
    private String name;

    private String apiIndex;
    private String primaryCategory;

    @Min(value = 1, message = "Ilość musi wynosić co najmniej 1")
    private int quantity;

    private String damage;
    private String twoHandedDamage;
//    private String properties;
}
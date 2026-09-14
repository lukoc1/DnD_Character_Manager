package pl.visa.dndCM.avatar;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvatarEquipmentItemDTO {

    private String name;
    private int quantity;
    private String category;
    private String damageDice;
    private String damageType;
    private Integer atkBonus;
}

package pl.visa.dndCM.equipmentItem;

import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.avatar.Avatar;
import pl.visa.dndCM.equipmentItem.damageType.DamageType;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Weapon extends EquipmentItem {

//    private Double weight;

//    private String cost;
    private String damage;
    private String twoHandedDamage;

    @ManyToOne
    private DamageType damageType;

//    private String properties;

//    @ManyToOne
//    @JoinColumn(name = "avatar_id")
//    private Avatar avatar;


}

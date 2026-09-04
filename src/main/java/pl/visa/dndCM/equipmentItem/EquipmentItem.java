package pl.visa.dndCM.equipmentItem;

import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.avatar.Avatar;
import pl.visa.dndCM.equipmentItem.damageType.DamageType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apiIndex;
    private String name;
    private String primaryCategory;

    private int quantity;
//    private Double weight;

//    private String cost;
    private String damage;
    private String twoHandedDamage;

    @ManyToOne
    private DamageType damageType;

    private String properties;

    @ManyToOne
    private Avatar avatar;
}

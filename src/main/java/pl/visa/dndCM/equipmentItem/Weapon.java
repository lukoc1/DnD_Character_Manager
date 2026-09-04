package pl.visa.dndCM.equipmentItem;

import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.equipmentItem.damageType.DamageType;

@Entity
@Table(name = "weapon_api")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Weapon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "equipment_item_id")
    private EquipmentItem equipmentItem;

    private String damage;
    private String twoHandedDamage;

    @ManyToOne
    private DamageType damageType;

}

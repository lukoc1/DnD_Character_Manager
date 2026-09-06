package pl.visa.dndCM.gameData.equipmentItem;

import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageType;

@Entity
@Table(name = "equipment_item_api")
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

    @Enumerated(EnumType.STRING)
    private EquipmentCategory category;

    private String damage;
    private String twoHandedDamage;

    @ManyToOne
    private DamageType damageType;

}

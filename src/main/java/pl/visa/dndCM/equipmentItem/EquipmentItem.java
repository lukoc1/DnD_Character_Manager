package pl.visa.dndCM.equipmentItem;

import jakarta.persistence.*;
import lombok.*;
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

    @Enumerated(EnumType.STRING)
    private EquipmentCategory category;

    private String damage;
    private String twoHandedDamage;

    @ManyToOne
    private DamageType damageType;

}

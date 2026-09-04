package pl.visa.dndCM.equipmentItem;

import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.avatar.Avatar;
import pl.visa.dndCM.equipmentItem.damageType.DamageType;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class EquipmentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apiIndex;
    private String name;
    private String category;

}

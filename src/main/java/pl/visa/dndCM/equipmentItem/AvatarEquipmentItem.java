package pl.visa.dndCM.equipmentItem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.visa.dndCM.avatar.Avatar;

@Entity
@Table(name = "avatar_equipment_item")
@Setter
@Getter
public class AvatarEquipmentItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    @ManyToOne
    @JoinColumn(name = "equipment_item_id")
    private EquipmentItem equipmentItem;

    private int quantity;
}
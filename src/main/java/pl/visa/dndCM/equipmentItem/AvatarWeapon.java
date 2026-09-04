package pl.visa.dndCM.equipmentItem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.visa.dndCM.avatar.Avatar;

@Entity
@Setter
@Getter
public class AvatarWeapon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    @ManyToOne
    @JoinColumn(name = "weapon_id")
    private Weapon weapon;

    private int quantity;
}
package pl.visa.dndCM.equipmentItem.damageType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "damage_type_api")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DamageType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apiIndex;
    private String name;

}

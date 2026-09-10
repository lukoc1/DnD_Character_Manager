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

    @Column(columnDefinition = "TEXT")
    private String description;

    // open5e "category.key", e.g. "weapon" / "armor" / "adventuring-gear" / "tools" / ...
    private String category;

    private String size;
    private Double weight;
    private String weightUnit;
    private Double cost;

    private String damageDice;

    @ManyToOne
    private DamageType damageType;

    private boolean simple;
    private boolean martial;
    private boolean improvised;
    private String distanceUnit;

    private String armorCategory;
    private Integer acBase;
    private String acDisplay;
    private boolean acAddDexMod;
    private Integer acCapDexMod;
    private boolean grantsStealthDisadvantage;
    private Integer strengthScoreRequired;

}

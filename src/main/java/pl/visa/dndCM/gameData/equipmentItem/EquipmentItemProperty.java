package pl.visa.dndCM.gameData.equipmentItem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipment_item_property")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentItemProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "equipment_item_id")
    @JsonIgnore
    private EquipmentItem equipmentItem;

    private String name;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String detail;
}

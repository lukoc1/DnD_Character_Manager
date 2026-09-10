package pl.visa.dndCM.gameData.dndClass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/** One skill a class can pick from, e.g. Barbarian: Athletics / Perception / Survival / ... */
@Entity
@Table(name = "dnd_class_skill_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DndClassSkillOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dnd_class_id")
    @JsonIgnore
    private DndClass dndClass;

    private String name;
}

package pl.visa.dndCM.gameData.dndClass;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dnd_class_level_table_entry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DndClassLevelTableEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dnd_class_id")
    private DndClass dndClass;

    // e.g. "Proficiency Bonus", "Rages", "Rage Damage", "Weapon Mastery"
    private String columnName;

    private int level;

    // text, not a number - values can be e.g. "+2"
    private String value;
}

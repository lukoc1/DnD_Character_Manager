package pl.visa.dndCM.gameData.background;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * One entry from a background's "benefits" (already structured, no parsing), e.g.
 * name "Skill Proficiencies", desc "Insight and Religion", type "skill_proficiency".
 */
@Entity
@Table(name = "background_benefit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackgroundBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "background_id")
    @JsonIgnore
    private Background background;

    // e.g. "skill_proficiency", "ability_score", "equipment", "feat", "tool_proficiency"
    private String type;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
}

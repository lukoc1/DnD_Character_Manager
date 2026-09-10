package pl.visa.dndCM.gameData.feature;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/** One level a feature is gained at, e.g. Ability Score Improvement -> 4, 8, 12, 16. */
@Entity
@Table(name = "class_feature_level")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DndClassFeatureLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_feature_id")
    @JsonIgnore
    private DndClassFeature feature;

    private int level;
}

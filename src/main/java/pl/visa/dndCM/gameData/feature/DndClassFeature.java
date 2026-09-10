package pl.visa.dndCM.gameData.feature;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.gameData.dndClass.DndClass;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclass;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "class_feature_api")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DndClassFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apiIndex;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // levels the feature is gained at (open5e "gained_at" - can be several, e.g. 4/8/12/16)
    @OneToMany(mappedBy = "feature")
    private List<DndClassFeatureLevel> levelsGained = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "dnd_class_id")
    private DndClass dndClass;

    @ManyToOne
    @JoinColumn(name = "subclass_id")
    private DndSubclass subclass;
}

package pl.visa.dndCM.gameData.feature;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.gameData.dndClass.DndClass;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclass;

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

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private int level;

    @ManyToOne
    @JoinColumn(name = "dnd_class_id")
    private DndClass dndClass;

    @ManyToOne
    @JoinColumn(name = "subclass_id")
    private DndSubclass subclass;
}

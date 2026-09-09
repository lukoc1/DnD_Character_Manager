package pl.visa.dndCM.gameData.dndSubclass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.gameData.dndClass.DndClass;

@Entity
@Table(name = "subclasses_api")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DndSubclass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apiIndex;
    private String name;

    @ManyToOne
    @JoinColumn(name = "dnd_class_id")
    private DndClass dndClass;
}

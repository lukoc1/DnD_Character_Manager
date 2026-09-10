package pl.visa.dndCM.gameData.specie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "specie_trait")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecieTrait {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "specie_id")
    @JsonIgnore
    private Specie specie;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // e.g. "SIZE", "SPEED", null
    private String type;

    private int traitOrder;
}

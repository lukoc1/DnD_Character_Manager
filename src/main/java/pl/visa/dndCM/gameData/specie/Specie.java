package pl.visa.dndCM.gameData.specie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.avatar.Avatar;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "species_api")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apiIndex;
    private String name;

    // parsed from the "SIZE" trait's description
    private String size;

    // parsed from the "SPEED" trait's description (e.g. "30 feet" -> 30)
    private int baseSpeed;

    @OneToMany(mappedBy = "specie")
    @JsonIgnore
    private List<Avatar> avatars = new ArrayList<>();

    @OneToMany(mappedBy = "specie")
    private List<SpecieTrait> traits = new ArrayList<>();

}

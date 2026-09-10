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

    // parent species when this is a subspecies (e.g. High Elf -> Elf), from open5e "subspecies_of"
    @ManyToOne
    @JoinColumn(name = "parent_specie_id")
    private Specie parentSpecie;

    @OneToMany(mappedBy = "specie")
    @JsonIgnore
    private List<Avatar> avatars = new ArrayList<>();
}

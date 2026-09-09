package pl.visa.dndCM.gameData.dndClass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.avatar.Avatar;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "classes_api")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DndClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apiIndex;
    private String name;

    @OneToMany(mappedBy = "dndClass")
    @JsonIgnore
    private List<Avatar> avatars = new ArrayList<>();
}

package pl.visa.dndCM.gameData.background;

import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.avatar.Avatar;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "background_api")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Background {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apiIndex;
    private String name;

    @OneToMany(mappedBy = "background")
    private List<Avatar> avatars = new ArrayList<>();
}

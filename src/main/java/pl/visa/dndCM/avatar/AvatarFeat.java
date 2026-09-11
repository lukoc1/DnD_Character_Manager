package pl.visa.dndCM.avatar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/** One feat the avatar has, e.g. the origin feat granted by the background: "Magic Initiate (Cleric)". */
@Entity
@Table(name = "avatar_feat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvatarFeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "avatar_id")
    @JsonIgnore
    private Avatar avatar;

    private String name;
}

package pl.visa.dndCM.avatar.proficiency;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.avatar.Avatar;

// one skill the avatar is proficient in, e.g. picked during class step: "Athletics"
@Entity
@Table(name = "avatar_skill_proficiency")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvatarSkillProficiency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "avatar_id")
    @JsonIgnore
    private Avatar avatar;

    private String name;
}

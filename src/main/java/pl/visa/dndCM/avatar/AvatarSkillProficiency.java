package pl.visa.dndCM.avatar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/** One skill the avatar is proficient in, e.g. picked during class step: "Athletics". */
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

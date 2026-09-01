package pl.visa.dndCM.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import pl.visa.dndCM.avatar.Avatar;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private UUID uuid;
    @NotBlank
    @Column(unique = true)
    private String name;
    @NotBlank
    private String password;

    @OneToMany(mappedBy = "owner")
    private List<Avatar> avatarList;
}

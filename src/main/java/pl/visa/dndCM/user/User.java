package pl.visa.dndCM.user;

import jakarta.persistence.*;
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
    private String name;
    private String password;

    @OneToMany(mappedBy = "owner")
    private List<Avatar> avatarList;
}

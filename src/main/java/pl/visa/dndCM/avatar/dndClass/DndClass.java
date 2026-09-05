package pl.visa.dndCM.avatar.dndClass;

import jakarta.persistence.*;
import lombok.*;


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
}

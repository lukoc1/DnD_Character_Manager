package pl.visa.dndCM.avatar;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.visa.dndCM.user.User;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    // // header
    // Basics
    private String name;
    private String background;
    private String className;
    private String species;
    private String subclassName;
    // // Level
//    private int level;
//    // Armor
//    private int armorClass;
//    private Boolean shield;
//    // Hit Points
//    private int maxHP;
//    private int currentHP;
//    private int tempHP;
//    // Stats
//    private int proficiencyBonus;
//
//    private int strMod;
//    private int strSco;
//
//    private int intMod;
//    private int intSco;
//
//    private int dexMod;
//    private int dexSco;
//
//    private int wisMod;
//    private int wisSco;
//
//    private int consMod;
//    private int consSco;
//
//    private int charMod;
//    private int charSco;



}

package pl.visa.dnd_character_manager.character;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.visa.dnd_character_manager.user.User;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DndCharacter {

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

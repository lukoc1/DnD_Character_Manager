package pl.visa.dndCM.avatar;

import jakarta.persistence.*;

import lombok.*;

import pl.visa.dndCM.gameData.background.Background;
import pl.visa.dndCM.gameData.dndClass.DndClass;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclass;
import pl.visa.dndCM.gameData.specie.Specie;
import pl.visa.dndCM.user.User;

import java.util.ArrayList;
import java.util.List;

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
    private User user;

    // // header
    // Basics
    private String name;

    @ManyToOne
    @JoinColumn(name = "background_id")
    private Background background;

    @ManyToOne
    @JoinColumn(name = "dnd_class_id")
    private DndClass dndClass;

    @ManyToOne
    @JoinColumn(name = "dnd_subclass_id")
    private DndSubclass Dndsubclass;

    @ManyToOne
    @JoinColumn(name = "specie_id")
    private Specie specie;

    private String subclassName;

    // // Level
    private int level;

    @OneToMany(mappedBy = "avatar")
    private List<AvatarEquipmentItem> equipmentItems = new ArrayList<>();


//    @ManyToMany
//    @JoinTable(name = "avatar_weapon",
//            joinColumns = @JoinColumn(name = "avatar_id"),
//            inverseJoinColumns = @JoinColumn(name = "weapon_id"))
//    private List<Weapon> weaponList = new ArrayList<>();


//    // Armor
    private int armorClass;
//    private Boolean shield;
//    // Hit Points
    private int maxHP;
    private int currentHP;
    private int tempHP;
//    // Stats
    private int proficiencyBonus;
//
    private int strMod;
    private int strSco;

    private int intMod;
    private int intSco;

    private int dexMod;
    private int dexSco;

    private int wisMod;
    private int wisSco;

    private int consMod;
    private int consSco;

    private int charMod;
    private int charSco;



}

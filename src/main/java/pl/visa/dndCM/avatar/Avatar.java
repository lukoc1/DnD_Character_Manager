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

    // still going through the creation wizard - hidden from listings, wiped when a new one starts
    private boolean draft;

    // class step choices
    @OneToMany(mappedBy = "avatar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvatarSkillProficiency> skillProficiencies = new ArrayList<>();

    // "A" / "B" chosen in the class step
    private String startingEquipmentChoice;

    // raw text of the picked option, kept per source (also resolved into equipmentItems + gold)
    @Column(columnDefinition = "TEXT")
    private String startingEquipmentClass;

    @Column(columnDefinition = "TEXT")
    private String startingEquipmentBackground;

    private int gold;

    @OneToMany(mappedBy = "avatar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvatarEquipmentItem> equipmentItems = new ArrayList<>();

    // feats the avatar has (for now just the origin feat from the background)
    @OneToMany(mappedBy = "avatar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvatarFeat> feats = new ArrayList<>();


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

package pl.visa.dndCM.avatar;

import jakarta.persistence.*;

import lombok.*;

import pl.visa.dndCM.avatar.equipment.AvatarEquipmentItem;
import pl.visa.dndCM.avatar.feat.AvatarFeat;
import pl.visa.dndCM.avatar.proficiency.AvatarSkillProficiency;
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

    private int level;

    // if character creation not completed - can be deleted by admin
    private boolean draft;

    // Equipment / creation-wizard related
    @OneToMany(mappedBy = "avatar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvatarSkillProficiency> skillProficiencies = new ArrayList<>();

    // text of the picked option A/B in class step
    private String startingEquipmentClass;

    // text of the picked option A/B in background step
    private String startingEquipmentBackground;

    @OneToMany(mappedBy = "avatar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvatarEquipmentItem> equipmentItems = new ArrayList<>();

    @OneToMany(mappedBy = "avatar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvatarFeat> feats = new ArrayList<>();

    private int gold;

    // Combat / HP
    private int armorClass;
    private int maxHP;
    private int currentHP;
    private int tempHP;
    private int hitDiceSpent;
    private String size;
    private int currentSpeed;

    // Ability scores + proficiency
    private int proficiencyBonus;

    private int strMod;
    private int strSco;

    private int dexMod;
    private int dexSco;

    private int consMod;
    private int consSco;

    private int intMod;
    private int intSco;

    private int wisMod;
    private int wisSco;

    private int charMod;
    private int charSco;
}

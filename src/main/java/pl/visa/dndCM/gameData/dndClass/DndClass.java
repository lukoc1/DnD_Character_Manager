package pl.visa.dndCM.gameData.dndClass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.avatar.Avatar;

import java.util.ArrayList;
import java.util.List;


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

    // hit die e.g. "D12" -> 12
    private int hitDiceValue;

    // "NONE" / "FULL" / "HALF"
    private String casterType;

    private boolean strSavingThrow;
    private boolean dexSavingThrow;
    private boolean consSavingThrow;
    private boolean intSavingThrow;
    private boolean wisSavingThrow;
    private boolean chaSavingThrow;

    private int skillChoiceCount;

    private String primaryAbility;
    private String weaponProficiencies;
    private String armorTraining;

    private String startingEquipmentA;
    private String startingEquipmentB;

    @OneToMany(mappedBy = "dndClass")
    private List<DndClassSkillOption> skillOptions = new ArrayList<>();

    @OneToMany(mappedBy = "dndClass")
    @JsonIgnore
    private List<Avatar> avatars = new ArrayList<>();

    /** Ability names this class grants saving throw proficiency in, e.g. ["Strength", "Constitution"]. */
    public List<String> getSavingThrowAbilities() {
        List<String> abilities = new ArrayList<>();
        if (strSavingThrow) abilities.add("Strength");
        if (dexSavingThrow) abilities.add("Dexterity");
        if (consSavingThrow) abilities.add("Constitution");
        if (intSavingThrow) abilities.add("Intelligence");
        if (wisSavingThrow) abilities.add("Wisdom");
        if (chaSavingThrow) abilities.add("Charisma");
        return abilities;
    }
}

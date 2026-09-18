package pl.visa.dndCM.avatar;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.visa.dndCM.avatar.equipment.AvatarEquipmentItemDTO;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvatarDTO {

    private Long id;
    private Long userId;

    private String user;

    @NotNull(message = "Class selection is required.")
    private Long dndClassId;

    @NotNull(message = "Background selection is required.")
    private Long backgroundId;

    @NotNull(message = "Specie selection is required.")
    private Long specieId;

    @NotBlank(message = "Every hero should have a name.")
    @Size(min = 2, max = 30, message = "Name length should be between 2 and 30 characters.")
    private String name;

    private int level;
    private boolean draft;

    // tylko do wyświetlania
    private String className;
    private String backgroundName;
    private String specieName;
    private String subclassName;

    // Equipment / creation-wizard related
    private int gold;

    private List<AvatarEquipmentItemDTO> equipment; // weapons + other items
    private List<AvatarEquipmentItemDTO> items; // non weapon
    private List<AvatarEquipmentItemDTO> weapons;

    private List<AvatarClassFeatureDTO> classFeatures;
    private List<AvatarSpecieTraitDTO> specieTraits;
    private List<String> feats;

    // Combat / HP
    private int armorClass;
    private int maxHP;
    private int currentHP;
    private int tempHP;
    private int hitDiceSpent;
    private int hitDieSize; // e.g. D12 -> 12
    private String size;
    private int currentSpeed;
    private String armorTraining;

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

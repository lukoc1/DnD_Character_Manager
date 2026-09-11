package pl.visa.dndCM.avatar;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvatarDTO {

    private Long id;
    private Long userId;

    private String user;

    // wybrana klasa (id z formularza)
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

    // tylko do wyświetlania
    private String className;
    private String backgroundName;

    private String specieName;
    private String subclassName;

    private int armorClass;
    //    private Boolean shield;
//    // Hit Points
    private int maxHP;
    private int currentHP;
    private int tempHP;

    private int hitDiceSpent;
    private int hitDieSize; // "e.g. D12 -> 12
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

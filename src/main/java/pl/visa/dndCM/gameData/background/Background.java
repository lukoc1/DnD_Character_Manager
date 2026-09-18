package pl.visa.dndCM.gameData.background;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.avatar.Avatar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "background_api")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Background {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apiIndex;
    private String name;

    @OneToMany(mappedBy = "background")
    @JsonIgnore
    private List<Avatar> avatars = new ArrayList<>();

    @OneToMany(mappedBy = "background")
    private List<BackgroundBenefit> benefits = new ArrayList<>();

    // the benefit of a given type (e.g. "feat", "equipment", "ability_score"), or null
    // e.g. http://127.0.0.1:8000/v2/backgrounds/?srd-2024_barbarian
    public BackgroundBenefit getBenefit(String type) {
        return benefits.stream()
                .filter(b -> type.equals(b.getType()))
                .findFirst()
                .orElse(null);
    }

    // three abilities this background can raise (e.g. ("Intelligence, Wisdom, Charisma"))
    public List<String> getAbilityScoreOptions() {
        BackgroundBenefit benefit = getBenefit("ability_score");

        if (benefit == null || benefit.getDescription() == null) {
            return List.of();
        }

        return Arrays.stream(benefit.getDescription().split(","))
                .map(b -> b.trim())
                .filter(b -> !b.isEmpty())
                .toList();
    }

    // background equipment package option A
    public String getEquipmentOptionA() {
        return equipmentOption(0);
    }

    // background equipment package option B
    public String getEquipmentOptionB() {
        return equipmentOption(1);
    }


    // e.g. "(A) A dagger, a set of thieves' tools, and 15 GP; or (B) 50 GP".
    private String equipmentOption(int option) {
        BackgroundBenefit benefit = getBenefit("equipment");

        if (benefit == null || benefit.getDescription() == null) {
            return null;
        }

        String desc = benefit.getDescription();
        int aStart = desc.indexOf("(A)");
        if (aStart < 0) {
            return option == 0 ? desc : null;
        }

        String[] parts = desc.substring(aStart + 3).split(";\\s*or\\s*\\(B\\)\\s*", 2);
        if (option == 0) {
            return parts[0].trim();
        }

        if (parts.length > 1) {
            return parts[1].trim();
        } else {
            // option B(1) but there is no option B
            return null;
        }
    }
}

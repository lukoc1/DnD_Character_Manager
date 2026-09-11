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

    /** The benefit of a given type (e.g. "feat", "equipment", "ability_score"), or null. */
    public BackgroundBenefit getBenefit(String type) {
        return benefits.stream()
                .filter(b -> type.equals(b.getType()))
                .findFirst()
                .orElse(null);
    }

    /** The three ability names this background can raise, e.g. ["Intelligence", "Wisdom", "Charisma"]. */
    public List<String> getAbilityScoreOptions() {
        BackgroundBenefit benefit = getBenefit("ability_score");
        if (benefit == null || benefit.getDescription() == null) {
            return List.of();
        }
        return Arrays.stream(benefit.getDescription().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    /** Equipment package (side A of the "*Choose A or B:* (A) ... ; or (B) 50 GP" text). */
    public String getEquipmentOptionA() {
        return equipmentOption(0);
    }

    /** The "50 GP" alternative (side B), or null if the background does not offer one. */
    public String getEquipmentOptionB() {
        return equipmentOption(1);
    }

    private String equipmentOption(int index) {
        BackgroundBenefit benefit = getBenefit("equipment");
        if (benefit == null || benefit.getDescription() == null) {
            return null;
        }
        String desc = benefit.getDescription();
        int aStart = desc.indexOf("(A)");
        if (aStart < 0) {
            return index == 0 ? desc : null;
        }
        String[] parts = desc.substring(aStart + 3).split(";\\s*or\\s*\\(B\\)\\s*", 2);
        if (index == 0) {
            return parts[0].trim();
        }
        return parts.length > 1 ? parts[1].trim() : null;
    }
}

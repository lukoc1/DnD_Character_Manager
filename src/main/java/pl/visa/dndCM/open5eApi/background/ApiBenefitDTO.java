package pl.visa.dndCM.open5eApi.background;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/** One entry from a background's "benefits" (e.g. name "Skill Proficiencies", desc "Insight and Religion", type "skill_proficiency"). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiBenefitDTO {

    private String name;
    private String desc;
    private String type;
}

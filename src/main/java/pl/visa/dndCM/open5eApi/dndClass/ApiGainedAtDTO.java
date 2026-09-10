package pl.visa.dndCM.open5eApi.dndClass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * One entry from a feature's "gained_at" - a level the feature is gained at. One feature
 * can have few (e.g. Ability Score Improvement at 4/8/12/16).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiGainedAtDTO {

    private int level;
    private String detail;
}

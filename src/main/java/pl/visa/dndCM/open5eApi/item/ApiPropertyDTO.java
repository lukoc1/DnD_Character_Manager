package pl.visa.dndCM.open5eApi.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * A weapon property: "Heavy", "Two-Handed", or a mastery like "Cleave".
 * "type" == "Mastery" for mastery properties, null for the rest.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiPropertyDTO {

    private String name;
    private String type;
    private String desc;
}

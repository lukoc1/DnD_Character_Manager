package pl.visa.dndCM.open5eApi.damageType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

// One entry from a damage type's "descriptions"
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiDamageDescriptionDTO {

    private String desc;
    private String document;

    // present in the API response, currently unused
    private String gamesystem;
}

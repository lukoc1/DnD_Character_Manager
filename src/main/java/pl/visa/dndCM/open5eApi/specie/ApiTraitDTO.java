package pl.visa.dndCM.open5eApi.specie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/** One entry from a species' "traits" (e.g. name "Speed", desc "30 feet", type "SPEED"). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiTraitDTO {

    private String name;
    private String desc;
    private String type;
    private int order;
}

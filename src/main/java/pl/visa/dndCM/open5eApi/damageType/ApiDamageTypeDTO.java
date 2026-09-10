package pl.visa.dndCM.open5eApi.damageType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

/** Response from /damagetypes */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiDamageTypeDTO {

    private String key;
    private String name;
    private List<ApiDamageDescriptionDTO> descriptions;
}

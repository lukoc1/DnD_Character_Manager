package pl.visa.dndCM.open5eApi.dndClass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/** Response from /classes.
 * Subclasses are in the same list, marked by a non-null "subclass_of". */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiClassDTO {

    private String key;
    private String name;

    @JsonProperty("hit_dice")
    private String hitDice;

    @JsonProperty("caster_type")
    private String casterType;

    @JsonProperty("saving_throws")
    private List<ApiSavingThrowDTO> savingThrows;

    @JsonProperty("subclass_of")
    private ApiClassRefDTO subclassOf;

    private List<ApiFeatureDTO> features;
}

package pl.visa.dndCM.open5eApi.specie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/** Response from /species.
 * Subspecies are in the same list, marked by a non-null "subspecies_of".
 * */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiSpecieDTO {

    private String key;
    private String name;

    @JsonProperty("is_subspecies")
    private boolean isSubspecies;

    @JsonProperty("subspecies_of")
    private ApiSpecieRefDTO subspeciesOf;

    private List<ApiTraitDTO> traits;
}

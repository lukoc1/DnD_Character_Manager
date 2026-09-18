package pl.visa.dndCM.open5eApi.specie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

// response from /species
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiSpecieDTO {

    private String key;
    private String name;

    private List<ApiTraitDTO> traits;
}

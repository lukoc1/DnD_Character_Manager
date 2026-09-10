package pl.visa.dndCM.open5eApi.specie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/** A species' "subspecies_of" - points to the parent species of a subspecies. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiSpecieRefDTO {

    private String key;
    private String name;
}

package pl.visa.dndCM.open5eApi.dndClass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/** A class's "subclass_of" - points to the base class of a subclass. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiClassRefDTO {

    private String key;
    private String name;
}

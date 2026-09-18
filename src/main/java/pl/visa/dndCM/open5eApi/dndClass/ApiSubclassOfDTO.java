package pl.visa.dndCM.open5eApi.dndClass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

// the "subclass_of" field on a class - null for a base class, points to the parent base class
// for a subclass entry (both key and name of that base class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiSubclassOfDTO {

    private String key;
    private String name;
}

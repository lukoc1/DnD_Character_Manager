package pl.visa.dndCM.open5eApi.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

// used for "category", "size" and "damage_type" on items
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiNamedKeyDTO {

    private String name;
    private String key;
}

package pl.visa.dndCM.open5eApi.dndClass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/** One entry from a class's "saving_throws" - just the ability name (e.g. "Strength"). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiSavingThrowDTO {

    private String name;
}

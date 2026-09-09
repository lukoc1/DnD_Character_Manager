package pl.visa.dndCM.gameData.dndSubclass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import pl.visa.dndCM.dnd5eapi.ApiReferenceDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DndSubclassDTO {

    private String index;
    private String name;

    @JsonProperty("class")
    private ApiReferenceDTO dndClass;
}

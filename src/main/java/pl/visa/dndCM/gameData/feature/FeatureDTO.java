package pl.visa.dndCM.gameData.feature;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import pl.visa.dndCM.dnd5eapi.ApiReferenceDTO;

/**
 * Odpowiedź ze szczegółu {@code /features/{index}} na dnd5eapi.co – cecha klasy
 * lub podklasy. {@link ApiReferenceDTO} tu nie wystarcza (zagnieżdżone
 * {@code "class"}, {@code "subclass"}, obiekt {@code "level"}). {@code subclass}
 * jest wypełnione tylko dla cech podklas; numer poziomu z {@code "level"} wyłuskuje
 * {@link FeatureImporter}. Mapowanie na encję robi {@link FeatureImporter}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureDTO {

    private String index;
    private String name;
    private String description;

    @JsonProperty("class")
    private ApiReferenceDTO dndClass;

    private ApiReferenceDTO subclass;

    private ApiReferenceDTO level;
}

package pl.visa.dndCM.dnd5eapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

/**
 * Kształt każdej odpowiedzi z dnd5eapi to
 * "count": x,
 * "results": [ { "index": x, "name": x, "url": x } ] }
 * Wszystkie "pośrednie" endpointy zwracają taką strukturę.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiListDTO {

    private int count;
    private List<ApiReferenceDTO> results;
}

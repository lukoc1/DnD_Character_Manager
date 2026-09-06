package pl.visa.dndCM.dnd5eapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiReferenceDTO {

    private String index;
    private String name;
    private String url;
}

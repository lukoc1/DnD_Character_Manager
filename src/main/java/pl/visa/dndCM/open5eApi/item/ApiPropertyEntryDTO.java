package pl.visa.dndCM.open5eApi.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/** One item in a weapon's "properties" list - the property plus an optional "detail". */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiPropertyEntryDTO {

    private ApiPropertyDTO property;
    private String detail;
}

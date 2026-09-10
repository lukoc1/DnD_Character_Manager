package pl.visa.dndCM.open5eApi.dndClass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/** One row of "data_for_class_table" (e.g. Rages at level 3 = "3"). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiTableRowDTO {

    private int level;

    @JsonProperty("column_value")
    private String columnValue;
}

package pl.visa.dndCM.open5eApi.dndClass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/** One entry from a class's "features". "feature_type" says what kind it is. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiFeatureDTO {

    private String key;
    private String name;
    private String desc;

    @JsonProperty("feature_type")
    private String featureType;

    @JsonProperty("gained_at")
    private List<ApiGainedAtDTO> gainedAt;

    @JsonProperty("data_for_class_table")
    private List<ApiTableRowDTO> dataForClassTable;
}

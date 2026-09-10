package pl.visa.dndCM.open5eApi.background;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

/** Response from /backgrounds. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiBackgroundDTO {

    private String key;
    private String name;
    private List<ApiBenefitDTO> benefits;
}

package pl.visa.dndCM.open5eApi.dndClass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

/** API pagination: {count, next, previous, results}. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiClassPageDTO {

    private int count;
    private String next;
    private String previous;
    private List<ApiClassDTO> results;
}

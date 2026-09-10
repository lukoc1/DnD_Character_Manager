package pl.visa.dndCM.open5eApi.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

/** Api pagination: {count, next, previous, results}. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiItemPageDTO {

    private int count;
    private String next;
    private String previous;
    private List<ApiItemDTO> results;
}

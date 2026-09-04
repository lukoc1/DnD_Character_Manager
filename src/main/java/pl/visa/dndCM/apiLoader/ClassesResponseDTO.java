package pl.visa.dndCM.apiLoader;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class ClassesResponseDTO {

    @JsonProperty("count")
    private int count;

    @JsonProperty("results")
    private List<ClassesDTO> results;
}
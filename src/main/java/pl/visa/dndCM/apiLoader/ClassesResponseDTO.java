package pl.visa.dndCM.apiLoader;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class ClassesResponseDTO {

    private int count;
    private List<ClassesDTO> results;
}
package pl.visa.dndCM.apiLoader;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class ClassesDTO {

    private String index;
    private String name;
    private String url;

}

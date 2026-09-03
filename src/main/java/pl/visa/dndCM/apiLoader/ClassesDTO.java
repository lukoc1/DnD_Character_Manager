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
public class ClassesDTO {


    // https://www.dnd5eapi.co/api/2024/classes;

    private String index;
    private String name;
    private String url;

}

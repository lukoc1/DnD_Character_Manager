package pl.visa.dndCM.apiLoader;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class Main {

    private static final String API_URL
        = "https://api.open5e.com/v2/items/{1}";
    private static final String API_URL_2
        = "https://www.dnd5eapi.co/api/2024/classes";

    public static void main(String[] args) throws Exception {

        RestTemplate restTemplate = UnsafeRestTemplate.create();

        ResponseEntity<ClassesResponseDTO> responseEntity = restTemplate.getForEntity(API_URL_2, ClassesResponseDTO.class);

        ClassesResponseDTO response = responseEntity.getBody();

        response.getResults().forEach(System.out::println);
    }
}

package pl.visa.dnd_character_manager.apiLoader;

import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class Main {

    private static final String API_URL
            = "https://www.dnd5eapi.co/api/classes";

    public static void main(String[] args) throws Exception {

        RestTemplate restTemplate = UnsafeRestTemplate.create();

        ClassesResponseDTO response = restTemplate.getForObject(
                API_URL,
                ClassesResponseDTO.class
        );

        if (response != null && response.getResults() != null) {
            response.getResults().forEach(System.out::println);
        }
    }
}

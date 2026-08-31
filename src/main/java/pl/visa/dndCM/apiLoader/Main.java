package pl.visa.dndCM.apiLoader;

import org.springframework.web.client.RestTemplate;

public class Main {

    private static final String API_URL
        = "https://api.open5e.com/v2/classes/?document__key__in=srd-2024";

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

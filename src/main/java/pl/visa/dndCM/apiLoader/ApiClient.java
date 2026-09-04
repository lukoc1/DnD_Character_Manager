package pl.visa.dndCM.apiLoader;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.visa.dndCM.equipmentItem.damageType.DamageType;
import pl.visa.dndCM.equipmentItem.damageType.DamageTypeListDTO;

@Service
public class ApiClient {

    private final RestTemplate restTemplate;
    private final String BASE_URL = "https://www.dnd5eapi.co/api/2024";

    // unsafeRestTemplate
    public ApiClient() {
        try {
            this.restTemplate = UnsafeRestTemplate.create();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DamageTypeListDTO getDamageTypes() {
        return restTemplate.getForObject(BASE_URL + "/damage-types", DamageTypeListDTO.class);
    }



}

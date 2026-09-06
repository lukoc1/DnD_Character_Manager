package pl.visa.dndCM.dnd5eapi;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.visa.dndCM.gameData.background.Background;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemDTO;

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

    // DamageTypes
    public ApiListDTO getDamageTypes() {
        return restTemplate.getForObject(BASE_URL + "/damage-types", ApiListDTO.class);
    }

    // EquipmentItem
    public ApiListDTO getEquipmentItems() {
        return restTemplate.getForObject(BASE_URL + "/equipment", ApiListDTO.class);
    }

    public EquipmentItemDTO getEquipmentItemDetail(String index) {
        return restTemplate.getForObject(BASE_URL + "/equipment/" + index, EquipmentItemDTO.class);
    }

    // DnDClasses
    public ApiListDTO getDndClasses() {
        return restTemplate.getForObject(BASE_URL + "/classes", ApiListDTO.class);
    }

    // Backgrounds
    public ApiListDTO getBackgrounds() {
        return restTemplate.getForObject(BASE_URL + "/backgrounds", ApiListDTO.class);
    }
}

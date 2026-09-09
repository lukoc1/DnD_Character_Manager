package pl.visa.dndCM.dnd5eapi;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclassDTO;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemDTO;
import pl.visa.dndCM.gameData.feature.FeatureDTO;

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

    // DnDSubclasses

    public ApiListDTO getDndSubclasses() {
        return restTemplate.getForObject(BASE_URL + "/subclasses", ApiListDTO.class);
    }

    public DndSubclassDTO getDndSubclassDetail(String index) {
        return restTemplate.getForObject(BASE_URL + "/subclasses/" + index, DndSubclassDTO.class);
    }

    // Features (cechy klas i podklas)
    public ApiListDTO getFeatures() {
        return restTemplate.getForObject(BASE_URL + "/features", ApiListDTO.class);
    }

    public FeatureDTO getFeatureDetail(String index) {
        return restTemplate.getForObject(BASE_URL + "/features/" + index, FeatureDTO.class);
    }


    // Backgrounds
    public ApiListDTO getBackgrounds() {
        return restTemplate.getForObject(BASE_URL + "/backgrounds", ApiListDTO.class);
    }

    // Species
    public ApiListDTO getSpecies() {
        return restTemplate.getForObject(BASE_URL + "/species", ApiListDTO.class);
    }
}

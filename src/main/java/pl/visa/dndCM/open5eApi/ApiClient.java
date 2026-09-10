package pl.visa.dndCM.open5eApi;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.visa.dndCM.open5eApi.background.ApiBackgroundPageDTO;
import pl.visa.dndCM.open5eApi.damageType.ApiDamageTypePageDTO;
import pl.visa.dndCM.open5eApi.dndClass.ApiClassPageDTO;
import pl.visa.dndCM.open5eApi.item.ApiItemPageDTO;
import pl.visa.dndCM.open5eApi.specie.ApiSpeciePageDTO;

@Service
public class ApiClient {

    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://127.0.0.1:8000/v2";

    public ApiClient() {
        try {
            this.restTemplate = UnsafeRestTemplate.create();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Classes + subclasses */
    public ApiClassPageDTO getClasses() {
        return restTemplate.getForObject(
                BASE_URL + "/classes/?document__key__in=srd-2024&limit=100", ApiClassPageDTO.class);
    }

    /** Backgrounds */
    public ApiBackgroundPageDTO getBackgrounds() {
        return restTemplate.getForObject(
                BASE_URL + "/backgrounds/?document__key__in=srd-2024&limit=100", ApiBackgroundPageDTO.class);
    }

    /** Damage types */
    public ApiDamageTypePageDTO getDamageTypes() {
        return restTemplate.getForObject(BASE_URL + "/damagetypes/?limit=100", ApiDamageTypePageDTO.class);
    }

    /** Species */
    public ApiSpeciePageDTO getSpecies() {
        return restTemplate.getForObject(
                BASE_URL + "/species/?document__key__in=srd-2024&limit=100", ApiSpeciePageDTO.class);
    }

    /** Items */
    public ApiItemPageDTO getItems() {
        return restTemplate.getForObject(
                BASE_URL + "/items/?document__key__in=srd-2024&limit=250", ApiItemPageDTO.class);
    }
}

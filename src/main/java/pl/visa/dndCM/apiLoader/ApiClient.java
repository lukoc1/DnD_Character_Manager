package pl.visa.dndCM.apiLoader;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.visa.dndCM.avatar.dndClass.DndClassListDTO;
import pl.visa.dndCM.equipmentItem.EquipmentItem;
import pl.visa.dndCM.equipmentItem.EquipmentItemDTO;
import pl.visa.dndCM.equipmentItem.EquipmentItemListDTO;
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

    // DamageTypes
    public DamageTypeListDTO getDamageTypes() {
        return restTemplate.getForObject(BASE_URL + "/damage-types", DamageTypeListDTO.class);
    }


    // EquipmentItem
    public EquipmentItemListDTO getEquipmentItems() {
        return restTemplate.getForObject(BASE_URL + "/equipment", EquipmentItemListDTO.class);
    }

    public EquipmentItemDTO getEquipmentItemDetail(String index) {
        return restTemplate.getForObject(BASE_URL + "/equipment/" + index, EquipmentItemDTO.class);
    }

    // DnDClasses
    public DndClassListDTO getDndClasses() {
        return restTemplate.getForObject(BASE_URL + "/classes", DndClassListDTO.class);
    }

}

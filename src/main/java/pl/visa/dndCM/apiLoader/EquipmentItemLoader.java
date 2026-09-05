package pl.visa.dndCM.apiLoader;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.equipmentItem.*;
import pl.visa.dndCM.equipmentItem.damageType.DamageTypeRepository;


@Service
@AllArgsConstructor
public class EquipmentItemLoader {

    private final ApiClient apiClient;
    private final EquipmentItemRepository equipmentItemRepository;
    private final DamageTypeRepository damageTypeRepository;

    public void loadEquipmentItems() {

        EquipmentItemListDTO data = apiClient.getEquipmentItems();

        data.getResults().stream()
                .filter(d -> !equipmentItemRepository.existsByApiIndex(d.getIndex()))
                .map(d -> toEntity(d.getIndex()))
                .forEach(d -> equipmentItemRepository.save(d));

    }

    private EquipmentItem toEntity(String index) {

        EquipmentItemDTO detail = apiClient.getEquipmentItemDetail(index);

        if (detail == null) {
            return null;
        }

        EquipmentItem equipmentItem = new EquipmentItem();
        equipmentItem.setApiIndex(detail.getIndex());
        equipmentItem.setName(detail.getName());


        if (detail.getCategory() != null) {
            equipmentItem.setCategory(detail.getCategory());
        } else {
            equipmentItem.setCategory(EquipmentCategory.ADVENTURING_GEAR);
        }

        if (detail.getDamage() != null) {
            equipmentItem.setDamage(detail.getDamage());

            if (detail.getDamageType() != null) {
                var damageTypes = damageTypeRepository.findByApiIndex(detail.getDamageType().getName());
                if (!damageTypes.isEmpty()) {
                    equipmentItem.setDamageType(damageTypes.get(0));
                }
            }
        }

        if (detail.getTwoHandedDamage() != null) {
            equipmentItem.setTwoHandedDamage(detail.getTwoHandedDamage());
        }

        return equipmentItem;
    }

}

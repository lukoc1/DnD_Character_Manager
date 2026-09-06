package pl.visa.dndCM.gameData.equipmentItem;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.dnd5eapi.ApiClient;
import pl.visa.dndCM.dnd5eapi.ApiListDTO;
import pl.visa.dndCM.dnd5eapi.ApiReferenceDTO;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageType;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageTypeRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EquipmentItemLoader {

    private final ApiClient apiClient;
    private final EquipmentItemRepository equipmentItemRepository;
    private final DamageTypeRepository damageTypeRepository;

    public void loadEquipmentItems() {

        ApiListDTO data = apiClient.getEquipmentItems();

        data.getResults().stream()
                .filter(d -> !equipmentItemRepository.existsByApiIndex(d.getIndex()))
                .map(d -> toEntity(d.getIndex()))
                .filter(Objects::nonNull)
                .forEach(equipmentItemRepository::save);
    }

    private EquipmentItem toEntity(String index) {

        EquipmentItemDTO detail = apiClient.getEquipmentItemDetail(index);
        if (detail == null) {
            return null;
        }

        EquipmentItem item = new EquipmentItem();
        item.setApiIndex(detail.getIndex());
        item.setName(detail.getName());
        item.setCategory(EquipmentCategory.fromApiCategories(categoryIndexes(detail)));

        if (detail.getDamage() != null) {
            item.setDamage(detail.getDamage().getDamageDice());
            resolveDamageType(detail.getDamage().getDamageType()).ifPresent(item::setDamageType);
        }

        if (detail.getTwoHandedDamage() != null) {
            item.setTwoHandedDamage(detail.getTwoHandedDamage().getDamageDice());
        }

        return item;
    }

    private List<String> categoryIndexes(EquipmentItemDTO detail) {
        if (detail.getEquipmentCategories() == null) {
            return List.of();
        }
        return detail.getEquipmentCategories().stream()
                .map(ApiReferenceDTO::getIndex)
                .toList();
    }

    private Optional<DamageType> resolveDamageType(ApiReferenceDTO damageTypeRef) {
        if (damageTypeRef == null) {
            return Optional.empty();
        }
        return damageTypeRepository.findByApiIndex(damageTypeRef.getIndex()).stream().findFirst();
    }
}

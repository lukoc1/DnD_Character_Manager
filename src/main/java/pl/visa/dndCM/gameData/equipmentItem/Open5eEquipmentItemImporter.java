package pl.visa.dndCM.gameData.equipmentItem;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.open5eApi.ApiClient;
import pl.visa.dndCM.open5eApi.item.ApiArmorDTO;
import pl.visa.dndCM.open5eApi.item.ApiItemDTO;
import pl.visa.dndCM.open5eApi.item.ApiNamedKeyDTO;
import pl.visa.dndCM.open5eApi.item.ApiPropertyEntryDTO;
import pl.visa.dndCM.open5eApi.item.ApiWeaponDTO;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageType;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageTypeRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class Open5eEquipmentItemImporter {

    private final ApiClient apiClient;
    private final EquipmentItemRepository equipmentItemRepository;
    private final EquipmentItemPropertyRepository equipmentItemPropertyRepository;
    private final DamageTypeRepository damageTypeRepository;

    public void importEquipmentItems() {

        List<ApiItemDTO> items = apiClient.getItems().getResults();

        items.forEach(this::importItem);
    }

    private void importItem(ApiItemDTO dto) {

        EquipmentItem item = equipmentItemRepository.findByApiIndex(dto.getKey()).orElseGet(EquipmentItem::new);
        item.setApiIndex(dto.getKey());
        item.setName(dto.getName());
        item.setDescription(dto.getDesc());
        item.setCategory(key(dto.getCategory()));
        item.setSize(name(dto.getSize()));
        item.setWeight(parseDouble(dto.getWeight()));
        item.setWeightUnit(dto.getWeightUnit());
        item.setCost(parseDouble(dto.getCost()));

        applyWeapon(item, dto.getWeapon());
        applyArmor(item, dto.getArmor());

        EquipmentItem saved = equipmentItemRepository.save(item);

        if (dto.getWeapon() != null) {
            importProperties(dto.getWeapon().getProperties(), saved);
        }
    }

    private void applyWeapon(EquipmentItem item, ApiWeaponDTO weapon) {
        if (weapon == null) {
            return;
        }

        item.setDamageDice(weapon.getDamageDice());
        item.setSimple(weapon.isSimple());
        item.setMartial(weapon.isMartial());
        item.setImprovised(weapon.isImprovised());
        item.setDistanceUnit(weapon.getDistanceUnit());

        if (weapon.getDamageType() != null) {
            damageTypeRepository.findByApiIndex(weapon.getDamageType().getKey()).stream()
                    .findFirst()
                    .ifPresent(item::setDamageType);
        }
    }

    private void applyArmor(EquipmentItem item, ApiArmorDTO armor) {
        if (armor == null) {
            return;
        }

        item.setArmorCategory(armor.getCategory());
        item.setAcBase(armor.getAcBase());
        item.setAcDisplay(armor.getAcDisplay());
        item.setAcAddDexMod(armor.isAcAddDexmod());
        item.setAcCapDexMod(armor.getAcCapDexmod());
        item.setGrantsStealthDisadvantage(armor.isGrantsStealthDisadvantage());
        item.setStrengthScoreRequired(armor.getStrengthScoreRequired());
    }

    private void importProperties(List<ApiPropertyEntryDTO> properties, EquipmentItem item) {
        if (properties == null || equipmentItemPropertyRepository.existsByEquipmentItem(item)) {
            return;
        }

        List<EquipmentItemProperty> rows = properties.stream()
                .map(entry -> EquipmentItemProperty.builder()
                        .equipmentItem(item)
                        .name(entry.getProperty().getName())
                        .type(entry.getProperty().getType())
                        .description(entry.getProperty().getDesc())
                        .detail(entry.getDetail())
                        .build())
                .toList();

        equipmentItemPropertyRepository.saveAll(rows);
    }

    private String key(ApiNamedKeyDTO ref) {
        return ref == null ? null : ref.getKey();
    }

    private String name(ApiNamedKeyDTO ref) {
        return ref == null ? null : ref.getName();
    }

    private Double parseDouble(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

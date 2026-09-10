package pl.visa.dndCM.gameData.equipmentItem.damageType;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.open5eApi.ApiClient;
import pl.visa.dndCM.open5eApi.damageType.ApiDamageDescriptionDTO;
import pl.visa.dndCM.open5eApi.damageType.ApiDamageTypeDTO;

import java.util.List;

@Service
@AllArgsConstructor
public class Open5eDamageTypeImporter {

    private final ApiClient apiClient;
    private final DamageTypeRepository damageTypeRepository;

    public void importDamageTypes() {

        List<ApiDamageTypeDTO> damageTypes = apiClient.getDamageTypes().getResults();

        damageTypes.forEach(this::importDamageType);
    }

    private void importDamageType(ApiDamageTypeDTO dto) {

        DamageType damageType = damageTypeRepository.findByApiIndex(dto.getKey()).stream()
                .findFirst()
                .orElseGet(DamageType::new);

        damageType.setApiIndex(dto.getKey());
        damageType.setName(dto.getName());
        damageType.setDescription(findDescription(dto.getDescriptions()));

        damageTypeRepository.save(damageType);
    }

    private String findDescription(List<ApiDamageDescriptionDTO> descriptions) {
        if (descriptions == null) {
            return null;
        }

        return descriptions.stream()
                .filter(d -> "srd-2024".equals(d.getDocument()))
                .map(ApiDamageDescriptionDTO::getDesc)
                .findFirst()
                .orElse(null);
    }
}

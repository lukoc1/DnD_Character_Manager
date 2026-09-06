package pl.visa.dndCM.gameData.equipmentItem.damageType;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.dnd5eapi.ApiClient;
import pl.visa.dndCM.dnd5eapi.ApiListDTO;
import pl.visa.dndCM.dnd5eapi.ApiReferenceDTO;

@Service
@AllArgsConstructor
public class DamageTypeImporter {

    private final ApiClient apiClient;
    private final DamageTypeRepository damageTypeRepository;

    public void importDamageTypes() {

        ApiListDTO data = apiClient.getDamageTypes();

        data.getResults().stream()
                .filter(d -> !damageTypeRepository.existsByApiIndex(d.getIndex()))
                .map(d -> toEntity(d))
                .forEach(d -> damageTypeRepository.save(d));

    }

    private DamageType toEntity(ApiReferenceDTO ref) {

        DamageType damageType = new DamageType();
        damageType.setApiIndex(ref.getIndex());
        damageType.setName(ref.getName());
        return damageType;
    }
}
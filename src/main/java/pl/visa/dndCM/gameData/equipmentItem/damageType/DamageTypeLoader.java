package pl.visa.dndCM.gameData.equipmentItem.damageType;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.dnd5eapi.ApiClient;
import pl.visa.dndCM.dnd5eapi.ApiListDTO;

@Service
@AllArgsConstructor
public class DamageTypeLoader {

    private final ApiClient apiClient;
    private final DamageTypeRepository damageTypeRepository;

    public void loadDamageTypes() {

        ApiListDTO data = apiClient.getDamageTypes();

        data.getResults().stream()
                .filter(d -> !damageTypeRepository.existsByApiIndex(d.getIndex()))
                .map(d -> {
                    DamageType damageType = new DamageType();
                    damageType.setApiIndex(d.getIndex());
                    damageType.setName(d.getName());

                    return damageType;
                }).forEach(d -> damageTypeRepository.save(d));

    }
}
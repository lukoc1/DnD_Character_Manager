package pl.visa.dndCM.apiLoader;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.equipmentItem.damageType.DamageType;
import pl.visa.dndCM.equipmentItem.damageType.DamageTypeDTO;
import pl.visa.dndCM.equipmentItem.damageType.DamageTypeListDTO;
import pl.visa.dndCM.equipmentItem.damageType.DamageTypeRepository;

@Service
@AllArgsConstructor
public class DamageTypeLoader {

    private final ApiClient apiClient;
    private final DamageTypeRepository damageTypeRepository;

    public void loadDamageTypes() {

        DamageTypeListDTO data = apiClient.getDamageTypes();

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
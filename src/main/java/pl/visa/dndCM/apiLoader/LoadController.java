package pl.visa.dndCM.apiLoader;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.visa.dndCM.equipmentItem.damageType.DamageType;
import pl.visa.dndCM.equipmentItem.damageType.DamageTypeRepository;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LoadController {

    private final DamageTypeLoader damageTypeLoader;
    private final DamageTypeRepository damageTypeRepository;

    public LoadController(DamageTypeLoader damageTypeLoader, DamageTypeRepository damageTypeRepository) {
        this.damageTypeLoader = damageTypeLoader;
        this.damageTypeRepository = damageTypeRepository;
    }

    @GetMapping("/load/damage-types")
    public String loadDamageTypes() {
        damageTypeLoader.loadDamageTypes();
        return "Damage types loaded";
    }

    @GetMapping("/show/damage-types")
    public List<DamageType> showDamageTypes() {
        return damageTypeRepository.findAll();
    }
}

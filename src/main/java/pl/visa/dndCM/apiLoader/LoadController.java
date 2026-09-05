package pl.visa.dndCM.apiLoader;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.visa.dndCM.avatar.dndClass.DndClass;
import pl.visa.dndCM.avatar.dndClass.DndClassRepository;
import pl.visa.dndCM.equipmentItem.EquipmentCategory;
import pl.visa.dndCM.equipmentItem.EquipmentItem;
import pl.visa.dndCM.equipmentItem.EquipmentItemRepository;
import pl.visa.dndCM.equipmentItem.damageType.DamageType;
import pl.visa.dndCM.equipmentItem.damageType.DamageTypeRepository;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class LoadController {

    private final DamageTypeLoader damageTypeLoader;
    private final DamageTypeRepository damageTypeRepository;

    private final EquipmentItemLoader equipmentItemLoader;
    private final EquipmentItemRepository equipmentItemRepository;

    private final DndClassLoader dndClassLoader;
    private final DndClassRepository dndClassRepository;



    // Damage types
    @GetMapping("/load/damage-types")
    public String loadDamageTypes() {
        damageTypeLoader.loadDamageTypes();
        return "Damage types loaded";
    }

    @GetMapping("/show/damage-types")
    public List<DamageType> showDamageTypes() {
        return damageTypeRepository.findAll();
    }


    // EquipmentItem

    @GetMapping("/load/equipment")
    public String loadEquipment() {
        equipmentItemLoader.loadEquipmentItems();
        return "Equipment items loaded";
    }


    // TRZEBA NA DTO
    @GetMapping("/show/equipment")
    public List<EquipmentItem> showEquipment() {
        return equipmentItemRepository.findAll();
    }

    @GetMapping("/show/equipment/{category}")
    public List<EquipmentItem> getEquipmentByCategory(@PathVariable EquipmentCategory category) {
        return equipmentItemRepository.findByCategory(category);
    }

    // Classes

    @GetMapping("/load/classes")
    public String loadClasses() {
        dndClassLoader.loadDndClasses();
        return "Dnd classses loaded";
    }

    @GetMapping("show/classes")
    public List<DndClass> showClasses() {
        return dndClassRepository.findAll();
    }



}

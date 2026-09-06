package pl.visa.dndCM.dnd5eapi;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.visa.dndCM.gameData.dndClass.DndClass;
import pl.visa.dndCM.gameData.dndClass.DndClassLoader;
import pl.visa.dndCM.gameData.dndClass.DndClassRepository;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentCategory;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItem;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemLoader;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemRepository;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageType;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageTypeLoader;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageTypeRepository;

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

    @GetMapping("/damage-types")
    public List<DamageType> getDamageTypes() {
        return damageTypeRepository.findAll();
    }


    // EquipmentItem

    @GetMapping("/load/equipment")
    public String loadEquipment() {
        equipmentItemLoader.loadEquipmentItems();
        return "Equipment items loaded";
    }

    // TRZEBA NA DTO
    @GetMapping("/equipment")
    public List<EquipmentItem> getEquipment() {
        return equipmentItemRepository.findAll();
    }

    @GetMapping("/equipment/{category}")
    public List<EquipmentItem> getEquipmentByCategory(@PathVariable EquipmentCategory category) {
        return equipmentItemRepository.findByCategory(category);
    }


    // Classes

    @GetMapping("/load/classes")
    public String loadClasses() {
        dndClassLoader.loadDndClasses();
        return "Dnd classes loaded";
    }

    @GetMapping("/classes")
    public List<DndClass> getClasses() {
        return dndClassRepository.findAll();
    }

}

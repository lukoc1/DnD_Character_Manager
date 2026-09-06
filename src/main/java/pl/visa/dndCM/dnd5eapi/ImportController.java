package pl.visa.dndCM.dnd5eapi;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.visa.dndCM.gameData.background.Background;
import pl.visa.dndCM.gameData.background.BackgroundImporter;
import pl.visa.dndCM.gameData.background.BackgroundRepository;
import pl.visa.dndCM.gameData.dndClass.DndClass;
import pl.visa.dndCM.gameData.dndClass.DndClassImporter;
import pl.visa.dndCM.gameData.dndClass.DndClassRepository;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentCategory;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItem;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemImporter;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemRepository;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageType;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageTypeImporter;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageTypeRepository;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ImportController {

    private final DamageTypeImporter damageTypeImporter;
    private final DamageTypeRepository damageTypeRepository;

    private final EquipmentItemImporter equipmentItemImporter;
    private final EquipmentItemRepository equipmentItemRepository;

    private final DndClassImporter dndClassImporter;
    private final DndClassRepository dndClassRepository;

    private final BackgroundImporter backgroundImporter;
    private final BackgroundRepository backgroundRepository;


    // Damage types

    @GetMapping("/import/damage-types")
    public String importDamageTypes() {
        damageTypeImporter.importDamageTypes();
        return "Damage types loaded";
    }

    @GetMapping("/damage-types")
    public List<DamageType> getDamageTypes() {
        return damageTypeRepository.findAll();
    }


    // EquipmentItem

    @GetMapping("/import/equipment")
    public String importEquipment() {
        equipmentItemImporter.importEquipmentItems();
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

    @GetMapping("/import/classes")
    public String importClasses() {
        dndClassImporter.importDndClasses();
        return "Dnd classes loaded";
    }

    @GetMapping("/classes")
    public List<DndClass> getClasses() {
        return dndClassRepository.findAll();
    }

    // Backgrounds

    @GetMapping("/import/backgrounds")
    public String importBackground() {
        backgroundImporter.importBackgrounds();
        return "Bacgrounds loaded";
    }

    @GetMapping("/backgrounds")
    public List<Background> getBackgrounds() {
        return backgroundRepository.findAll();
    }
}

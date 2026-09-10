package pl.visa.dndCM.open5eApi;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.visa.dndCM.gameData.background.Background;
import pl.visa.dndCM.gameData.background.BackgroundRepository;
import pl.visa.dndCM.gameData.background.Open5eBackgroundImporter;
import pl.visa.dndCM.gameData.dndClass.DndClass;
import pl.visa.dndCM.gameData.dndClass.DndClassRepository;
import pl.visa.dndCM.gameData.dndClass.Open5eClassImporter;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItem;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemRepository;
import pl.visa.dndCM.gameData.equipmentItem.Open5eEquipmentItemImporter;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageType;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageTypeRepository;
import pl.visa.dndCM.gameData.equipmentItem.damageType.Open5eDamageTypeImporter;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclass;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclassRepository;
import pl.visa.dndCM.gameData.feature.DndClassFeature;
import pl.visa.dndCM.gameData.feature.FeatureRepository;
import pl.visa.dndCM.gameData.specie.Open5eSpecieImporter;
import pl.visa.dndCM.gameData.specie.Specie;
import pl.visa.dndCM.gameData.specie.SpecieRepository;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ImportController {

    private final Open5eDamageTypeImporter damageTypeImporter;
    private final DamageTypeRepository damageTypeRepository;

    private final Open5eEquipmentItemImporter equipmentItemImporter;
    private final EquipmentItemRepository equipmentItemRepository;

    private final Open5eClassImporter open5eClassImporter;
    private final DndClassRepository dndClassRepository;

    private final Open5eBackgroundImporter open5eBackgroundImporter;
    private final BackgroundRepository backgroundRepository;

    private final DndSubclassRepository dndSubclassRepository;

    private final FeatureRepository featureRepository;

    private final Open5eSpecieImporter specieImporter;
    private final SpecieRepository specieRepository;



    // Everything at once, in dependency order (damage types before equipment)

    @GetMapping("/import/all")
    public String importAll() {
        damageTypeImporter.importDamageTypes();
        open5eClassImporter.importClasses();
        open5eBackgroundImporter.importBackgrounds();
        specieImporter.importSpecies();
        equipmentItemImporter.importEquipmentItems();
        return "All game data loaded";
    }


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

    // TODO: return a DTO, not the entity
    @GetMapping("/equipment")
    public List<EquipmentItem> getEquipment() {
        return equipmentItemRepository.findAll();
    }

    @GetMapping("/equipment/{category}")
    public List<EquipmentItem> getEquipmentByCategory(@PathVariable String category) {
        return equipmentItemRepository.findByCategory(category);
    }


    // Classes + subclasses + features + progression tables, from local open5e-api (v2)

    @GetMapping("/import/classes")
    public String importClasses() {
        open5eClassImporter.importClasses();
        return "Dnd classes loaded";
    }

    @GetMapping("/classes")
    public List<DndClass> getClasses() {
        return dndClassRepository.findAll();
    }

    // Backgrounds, from local open5e-api (v2), with full benefits

    @GetMapping("/import/backgrounds")
    public String importBackground() {
        open5eBackgroundImporter.importBackgrounds();
        return "Bacgrounds loaded";
    }

    @GetMapping("/backgrounds")
    public List<Background> getBackgrounds() {
        return backgroundRepository.findAll();
    }

    // Subclasses (imported together with classes, see /import/classes)

    @GetMapping("/subclasses")
    public List<DndSubclass> getSubclasses() {
        return dndSubclassRepository.findAll();
    }

    // Features (imported together with classes, see /import/classes)

    @GetMapping("/features")
    public List<DndClassFeature> getFeatures() {
        return featureRepository.findAll();
    }

    // Species

    @GetMapping("/import/species")
    public String importSpecies() {
        specieImporter.importSpecies();
        return "Species loaded";
    }

    @GetMapping("/species")
    public List<Specie> getSpecies() {
        return specieRepository.findAll();
    }
}

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
import pl.visa.dndCM.gameData.dndSubclass.DndSubclassImporter;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentCategory;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItem;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemImporter;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemRepository;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageType;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageTypeImporter;
import pl.visa.dndCM.gameData.equipmentItem.damageType.DamageTypeRepository;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclass;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclassRepository;
import pl.visa.dndCM.gameData.feature.DndClassFeature;
import pl.visa.dndCM.gameData.feature.FeatureImporter;
import pl.visa.dndCM.gameData.feature.FeatureRepository;
import pl.visa.dndCM.gameData.specie.Specie;
import pl.visa.dndCM.gameData.specie.SpecieImporter;
import pl.visa.dndCM.gameData.specie.SpecieRepository;

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

    private final DndSubclassImporter dndSubclassImporter;
    private final DndSubclassRepository dndSubclassRepository;

    private final FeatureImporter featureImporter;
    private final FeatureRepository featureRepository;

    private final SpecieImporter specieImporter;
    private final SpecieRepository specieRepository;



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

    // Subclasses (lista z dnd5eapi /subclasses/)

    @GetMapping("/import/subclasses")
    public String importSubclasses() {
        dndSubclassImporter.importDndSubclasses();
        return "Subclasses loaded";
    }

    @GetMapping("/subclasses")
    public List<DndSubclass> getSubclasses() {
        return dndSubclassRepository.findAll();
    }

    // Features (cechy klas i podklas z dnd5eapi /features/ — ~232 zapytania)

    @GetMapping("/import/features")
    public String importFeatures() {
        featureImporter.importFeatures();
        return "Features loaded";
    }

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

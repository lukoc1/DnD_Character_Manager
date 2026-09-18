package pl.visa.dndCM.gameData.dndClass;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.open5eApi.ApiClient;
import pl.visa.dndCM.open5eApi.dndClass.ApiClassDTO;
import pl.visa.dndCM.open5eApi.dndClass.ApiFeatureDTO;
import pl.visa.dndCM.open5eApi.dndClass.ApiSavingThrowDTO;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclass;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclassRepository;
import pl.visa.dndCM.gameData.feature.DndClassFeature;
import pl.visa.dndCM.gameData.feature.DndClassFeatureLevel;
import pl.visa.dndCM.gameData.feature.DndClassFeatureLevelRepository;
import pl.visa.dndCM.gameData.feature.FeatureRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class Open5eClassImporter {

    private final ApiClient apiClient;
    private final DndClassRepository dndClassRepository;
    private final DndSubclassRepository dndSubclassRepository;
    private final FeatureRepository featureRepository;
    private final DndClassFeatureLevelRepository featureLevelRepository;
    private final DndClassLevelTableEntryRepository levelTableEntryRepository;
    private final DndClassSkillOptionRepository skillOptionRepository;

    public void importClasses() {

        List<ApiClassDTO> classes = apiClient.getClasses().getResults();

        classes.stream()
                .filter(c -> c.getSubclassOf() == null)
                .forEach(c -> importBaseClass(c));

        classes.stream()
                .filter(c -> c.getSubclassOf() != null)
                .forEach(c -> importSubclass(c));
    }

    private void importBaseClass(ApiClassDTO dto) {

        DndClass dndClass = dndClassRepository.findByApiIndex(dto.getKey()).orElseGet(DndClass::new);
        dndClass.setApiIndex(dto.getKey());
        dndClass.setName(dto.getName());
        dndClass.setHitDiceValue(parseHitDie(dto.getHitDice()));
        dndClass.setCasterType(dto.getCasterType());
        applySavingThrows(dndClass, dto.getSavingThrows());
        List<String> skillNames = applyCoreTraits(dndClass, dto.getFeatures());

        DndClass saved = dndClassRepository.save(dndClass);

        importSkillOptions(skillNames, saved);
        importFeaturesAndTables(dto.getFeatures(), saved, null);
    }

    private void importSubclass(ApiClassDTO dto) {

        Optional<DndClass> parent = dndClassRepository.findByApiIndex(dto.getSubclassOf().getKey());
        if (parent.isEmpty()) {
            return;
        }

        DndSubclass subclass = dndSubclassRepository.findByApiIndex(dto.getKey()).orElseGet(DndSubclass::new);
        subclass.setApiIndex(dto.getKey());
        subclass.setName(dto.getName());
        subclass.setDndClass(parent.get());

        DndSubclass saved = dndSubclassRepository.save(subclass);

        importFeaturesAndTables(dto.getFeatures(), null, saved);
    }

    private void applySavingThrows(DndClass dndClass, List<ApiSavingThrowDTO> savingThrows) {
        if (savingThrows == null) {
            return;
        }

        for (ApiSavingThrowDTO savingThrow : savingThrows) {
            if (savingThrow.getName() == null) {
                continue;
            }

            switch (savingThrow.getName().toLowerCase()) {
                case "strength" -> {
                    dndClass.setStrSavingThrow(true);
                }
                case "dexterity" -> {
                    dndClass.setDexSavingThrow(true);
                }
                case "constitution" -> {
                    dndClass.setConsSavingThrow(true);
                }
                case "intelligence" -> {
                    dndClass.setIntSavingThrow(true);
                }
                case "wisdom" -> {
                    dndClass.setWisSavingThrow(true);
                }
                case "charisma" -> {
                    dndClass.setChaSavingThrow(true);
                }
                default -> {}
            }
        }
    }

    // reads the CORE_TRAITS_TABLE, sets the simple class fields (primary ability,
    // weapon proficiencies, armor training, starting equipment A/B, skill choice count) and
    // returns the skill names to choose
//    http://127.0.0.1:8000/v2/classes/?document__key__in=srd-2024&limit=100
    private List<String> applyCoreTraits(DndClass dndClass, List<ApiFeatureDTO> features) {
        if (features == null) {
            return List.of();
        }

        Optional<String> desc = features.stream()
                .filter(f -> "CORE_TRAITS_TABLE".equals(f.getFeatureType()))
                .findFirst()
                .map(f -> f.getDesc());

        if (desc.isEmpty()) {
            return List.of();
        }

        Map<String, String> traits = parseCoreTraitsTable(desc.get());

        dndClass.setPrimaryAbility(traits.get("Primary Ability"));
        dndClass.setWeaponProficiencies(traits.get("Weapon Proficiencies"));
        dndClass.setArmorTraining(traits.get("Armor Training"));
        applyStartingEquipment(dndClass, traits.get("Starting Equipment"));

        return parseSkillChoice(dndClass, traits.get("Skill Proficiencies"));
    }

    // turns "|Label|Value|" into map(label, value), skipping "|||" / "|---|---|"
    private Map<String, String> parseCoreTraitsTable(String markdown) {
        Map<String, String> traits = new HashMap<>();

        for (String line : markdown.split("\n")) {
            String[] columns = line.split("\\|");
            if (columns.length < 3) {
                continue;
            }

            String label = columns[1].trim();
            String value = columns[2].trim();
            if (label.isEmpty() || label.equals("---")) {
                continue;
            }

            traits.put(label, value);
        }

        return traits;
    }

    // "Choose 2: Arcana, History, ..., or Religion" -> sets skillChoiceCount, returns the names
    private List<String> parseSkillChoice(DndClass dndClass, String value) {
        // Bard issue -> for some reason looks different from others
        if (value == null || !value.startsWith("Choose ")) {
            return List.of();
        }

        int colonIndex = value.indexOf(':');
        if (colonIndex < 0) {
            return List.of();
        }

        // e.g. "Choose 2"
        try {
            dndClass.setSkillChoiceCount(Integer.parseInt(value.substring("Choose ".length(), colonIndex).trim()));
        } catch (NumberFormatException e) {
            return List.of();
        }

        // takes Skills after ":"
        return Arrays.stream(value.substring(colonIndex + 1).split(","))
                .map(s -> s.trim())
                .map(s -> s.replaceFirst("^or\\s+", ""))
                .filter(s -> !s.isEmpty())
                .toList();
    }

    // "Choose A or B" -> startingEquipmentA / startingEquipmentB
    private void applyStartingEquipment(DndClass dndClass, String value) {
        if (value == null) {
            return;
        }

        if (!value.contains("Choose ")) {
            dndClass.setStartingEquipmentA(value);
            return;
        }

        String body = value.substring(value.indexOf(':') + 1).trim();
        String[] parts = body.split(";\\s*or\\s*\\(B\\)\\s*");

        dndClass.setStartingEquipmentA(parts[0].replaceFirst("^\\(A\\)\\s*", "").trim());

        if (parts.length > 1) {
            dndClass.setStartingEquipmentB(parts[1].trim());
        } else {
            dndClass.setStartingEquipmentB(null);
        }
    }

    private void importSkillOptions(List<String> names, DndClass dndClass) {
        if (names.isEmpty() || skillOptionRepository.existsByDndClass(dndClass)) {
            return;
        }

        List<DndClassSkillOption> rows = names.stream()
                .map(name -> DndClassSkillOption.builder().dndClass(dndClass).name(name).build())
                .toList();

        skillOptionRepository.saveAll(rows);
    }

    // see e.g. "Barbarian Features" table (PlayersHandbook2024)
    private void importFeaturesAndTables(List<ApiFeatureDTO> features, DndClass dndClass, DndSubclass subclass) {
        if (features == null) {
            return;
        }

        for (ApiFeatureDTO feature : features) {
            switch (feature.getFeatureType()) {
                case "CLASS_LEVEL_FEATURE" -> importClassLevelFeature(feature, dndClass, subclass);
                // more convenient to keep all in one table than separate
                case "PROFICIENCY_BONUS", "CLASS_TABLE_DATA" -> importLevelTable(feature, dndClass);
                default -> {}
            }
        }
    }

    private void importClassLevelFeature(ApiFeatureDTO feature, DndClass dndClass, DndSubclass subclass) {
        if (featureRepository.existsByApiIndex(feature.getKey())) {
            return;
        }

        DndClassFeature entity = DndClassFeature.builder()
                .apiIndex(feature.getKey())
                .name(feature.getName())
                .description(feature.getDesc())
                .dndClass(dndClass)
                .subclass(subclass)
                .build();

        DndClassFeature saved = featureRepository.save(entity);

        if (feature.getGainedAt() == null) {
            return;
        }

        List<DndClassFeatureLevel> levels = feature.getGainedAt().stream()
                .map(g -> DndClassFeatureLevel.builder().feature(saved).level(g.getLevel()).build())
                .toList();

        featureLevelRepository.saveAll(levels);
    }

    private void importLevelTable(ApiFeatureDTO feature, DndClass dndClass) {
        if (dndClass == null || feature.getDataForClassTable() == null) {
            return;
        }
        if (levelTableEntryRepository.existsByDndClassAndColumnName(dndClass, feature.getName())) {
            return;
        }

        List<DndClassLevelTableEntry> rows = feature.getDataForClassTable().stream()
                .map(row -> DndClassLevelTableEntry.builder()
                        .dndClass(dndClass)
                        .columnName(feature.getName())
                        .level(row.getLevel())
                        .value(row.getColumnValue())
                        .build())
                .toList();

        levelTableEntryRepository.saveAll(rows);
    }

    private int parseHitDie(String hitDice) {
        if (hitDice == null) {
            return 0;
        }
        return Integer.parseInt(hitDice.replace("D", ""));

    }

}

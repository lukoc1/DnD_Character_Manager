package pl.visa.dndCM.avatar;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.exception.ErrorCode;
import pl.visa.dndCM.exception.ResourceNotFoundException;
import pl.visa.dndCM.gameData.dndClass.DndClass;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItem;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemProperty;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemPropertyRepository;
import pl.visa.dndCM.gameData.feature.DndClassFeature;
import pl.visa.dndCM.gameData.feature.DndClassFeatureLevel;
import pl.visa.dndCM.gameData.feature.FeatureRepository;
import pl.visa.dndCM.gameData.specie.SpecieTraitRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Core avatar lookups and DTO mapping. Creation lives in avatar.creation.AvatarCreationService,
// progression (level-up/subclass/ASI) in avatar.progression.AvatarProgressionService,
// HP/hit dice in avatar.health.AvatarHealthService, gold/items in avatar.inventory.AvatarInventoryService.
@Service
@AllArgsConstructor
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final AvatarSkillProficiencyRepository skillProficiencyRepository;
    private final AvatarFeatRepository avatarFeatRepository;
    private final EquipmentItemPropertyRepository equipmentItemPropertyRepository;
    private final FeatureRepository featureRepository;
    private final SpecieTraitRepository specieTraitRepository;

    public AvatarDTO getAvatarById(Long id) {
        return toDTO(getAvatarOrThrow(id));
    }

    public List<AvatarDTO> findAllByUserId(Long id) {
        return avatarRepository.findAllByUser_IdAndDraftFalse(id).stream()
                .map(this::toDTO).toList();
    }

    // names of the skills this avatar is proficient in - for ticking checkboxes on the sheet
    public Set<String> getSkillProficiencyNames(Long avatarId) {
        return skillProficiencyRepository.findByAvatar_Id(avatarId).stream()
                .map(AvatarSkillProficiency::getName)
                .collect(Collectors.toSet());
    }

    // ability names whose saving throw the avatar's class makes it proficient in (e.g. "Strength")
    public Set<String> getSavingThrowAbilities(Long avatarId) {
        Avatar avatar = getAvatarOrThrow(avatarId);

        DndClass dndClass = avatar.getDndClass();
        if (dndClass == null) {
            return new HashSet<>();
        }
        return new HashSet<>(dndClass.getSavingThrowAbilities());
    }

    public void deleteAvatarById(Long id) {
        avatarRepository.deleteById(id);
    }

    public int getPassivePerception(Long avatarId) {
        Avatar avatar = getAvatarOrThrow(avatarId);

        boolean proficientInPerception = skillProficiencyRepository.findByAvatar_Id(avatarId).stream()
                .anyMatch(p -> "Perception".equals(p.getName()));

        return 10 + avatar.getWisMod() + (proficientInPerception ? avatar.getProficiencyBonus() : 0);
    }

    Avatar getAvatarOrThrow(Long avatarId) {
        return avatarRepository.findById(avatarId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Avatar id=%s not found", avatarId), ErrorCode.AVATAR_NOT_FOUND));
    }

    public static int abilityModifier(int score) {
        return (int) Math.floor((score - 10) / 2.0);
    }

    // adds value to the given ability (capped at 20), updating its modifier - no-op for a null/unknown ability
    public static void addAbilityScore(Avatar avatar, String ability, int value) {
        if (ability == null) {
            return;
        }
        switch (ability) {
            case "Strength" -> {
                int v = Math.min(20, avatar.getStrSco() + value);
                avatar.setStrSco(v);
                avatar.setStrMod(abilityModifier(v));
            }
            case "Dexterity" -> {
                int v = Math.min(20, avatar.getDexSco() + value);
                avatar.setDexSco(v);
                avatar.setDexMod(abilityModifier(v));
            }
            case "Constitution" -> {
                int v = Math.min(20, avatar.getConsSco() + value);
                avatar.setConsSco(v);
                avatar.setConsMod(abilityModifier(v));
            }
            case "Intelligence" -> {
                int v = Math.min(20, avatar.getIntSco() + value);
                avatar.setIntSco(v);
                avatar.setIntMod(abilityModifier(v));
            }
            case "Wisdom" -> {
                int v = Math.min(20, avatar.getWisSco() + value);
                avatar.setWisSco(v);
                avatar.setWisMod(abilityModifier(v));
            }
            case "Charisma" -> {
                int v = Math.min(20, avatar.getCharSco() + value);
                avatar.setCharSco(v);
                avatar.setCharMod(abilityModifier(v));
            }
            default -> {}
        }
    }

    // attack bonus = ability modifier (Strength, or Dexterity for ranged/finesse weapons - whichever
    // is higher for finesse) + proficiency bonus, if the avatar's class is proficient with this weapon
    private Integer weaponAtkBonus(Avatar avatar, EquipmentItem item) {
        if (item == null || !"weapon".equals(item.getCategory())) {
            return null;
        }

        List<EquipmentItemProperty> properties = equipmentItemPropertyRepository.findByEquipmentItem(item);
        boolean finesse = properties.stream().anyMatch(p -> "Finesse".equalsIgnoreCase(p.getName()));

        int abilityMod;
        if (item.getDistanceUnit() != null) {
            abilityMod = avatar.getDexMod();
        } else if (finesse) {
            abilityMod = Math.max(avatar.getStrMod(), avatar.getDexMod());
        } else {
            abilityMod = avatar.getStrMod();
        }

        int proficiencyBonus = isWeaponProficient(avatar.getDndClass(), item, properties)
                ? avatar.getProficiencyBonus()
                : 0;

        return abilityMod + proficiencyBonus;
    }

    private boolean isWeaponProficient(DndClass dndClass, EquipmentItem item, List<EquipmentItemProperty> itemProperties) {
        String weaponProficiencies = dndClass == null ? null : dndClass.getWeaponProficiencies();
        if (weaponProficiencies == null) {
            return false;
        }
        String lower = weaponProficiencies.toLowerCase();

        if (item.isSimple() && lower.contains("simple")) {
            return true;
        }
        if (item.isMartial() && lower.contains("martial")) {
            if (lower.contains("that have")) {
                return itemProperties.stream().anyMatch(p -> lower.contains(p.getName().toLowerCase()));
            }
            return true;
        }
        return false;
    }


    /// mapping

    public AvatarDTO toDTO(Avatar avatar) {

        return AvatarDTO.builder()
                .id(avatar.getId())
                .name(avatar.getName())
                .backgroundName(avatar.getBackground().getName())
                .backgroundId(avatar.getBackground().getId())
                .dndClassId(avatar.getDndClass().getId())
                .className(avatar.getDndClass().getName())
                .specieName(avatar.getSpecie().getName())
                .specieId(avatar.getSpecie().getId())
                .subclassName(avatar.getSubclassName())
                .level(avatar.getLevel())
                .userId(avatar.getUser().getId())
                .armorClass(avatar.getArmorClass())

                .maxHP(avatar.getMaxHP())
                .currentHP(avatar.getCurrentHP())
                .tempHP(avatar.getTempHP())
                .hitDiceSpent(avatar.getHitDiceSpent())
                .hitDieSize(avatar.getDndClass().getHitDiceValue())
                .size(avatar.getSize())
                .currentSpeed(avatar.getCurrentSpeed())
                .armorTraining(avatar.getDndClass().getArmorTraining())

                .proficiencyBonus(avatar.getProficiencyBonus())
                .strMod(avatar.getStrMod())
                .strSco(avatar.getStrSco())
                .dexMod(avatar.getDexMod())
                .dexSco(avatar.getDexSco())
                .consMod(avatar.getConsMod())
                .consSco(avatar.getConsSco())
                .intMod(avatar.getIntMod())
                .intSco(avatar.getIntSco())
                .wisMod(avatar.getWisMod())
                .wisSco(avatar.getWisSco())
                .charMod(avatar.getCharMod())
                .charSco(avatar.getCharSco())

                .equipment(toEquipmentDTOs(avatar))
                .weapons(toWeaponDTOs(avatar))
                .items(toNonWeaponDTOs(avatar))
                .classFeatures(toClassFeatureDTOs(avatar))
                .specieTraits(toSpecieTraitDTOs(avatar))
                .feats(toFeatNames(avatar))
                .gold(avatar.getGold())

                .build();
    }

    private List<AvatarEquipmentItemDTO> toEquipmentDTOs(Avatar avatar) {
        return avatar.getEquipmentItems().stream()
                .map(e -> AvatarEquipmentItemDTO.builder()
                        .name(e.getEquipmentItem().getName())
                        .quantity(e.getQuantity())
                        .category(e.getEquipmentItem().getCategory())
                        .damageDice(e.getEquipmentItem().getDamageDice())
                        .damageType(e.getEquipmentItem().getDamageType() != null
                                ? e.getEquipmentItem().getDamageType().getName()
                                : null)
                        .atkBonus(weaponAtkBonus(avatar, e.getEquipmentItem()))
                        .build())
                .toList();
    }

    private List<AvatarEquipmentItemDTO> toWeaponDTOs(Avatar avatar) {
        return toEquipmentDTOs(avatar).stream()
                .filter(w -> "weapon".equals(w.getCategory()))
                .toList();
    }

    private List<AvatarEquipmentItemDTO> toNonWeaponDTOs(Avatar avatar) {
        return toEquipmentDTOs(avatar).stream()
                .filter(w -> !"weapon".equals(w.getCategory()))
                .toList();
    }

    private List<AvatarClassFeatureDTO> toClassFeatureDTOs(Avatar avatar) {
        List<DndClassFeature> features = new ArrayList<>(featureRepository.findByDndClass(avatar.getDndClass()));
        if (avatar.getDndsubclass() != null) {
            features.addAll(featureRepository.findBySubclass(avatar.getDndsubclass()));
        }

        return features.stream()
                .filter(f -> f.getLevelsGained().stream().anyMatch(l -> l.getLevel() <= avatar.getLevel()))
                .sorted(Comparator.comparing(f -> f.getLevelsGained().stream()
                        .mapToInt(DndClassFeatureLevel::getLevel).min().orElse(0)))
                .map(f -> AvatarClassFeatureDTO.builder()
                        .name(f.getName())
                        .description(f.getDescription())
                        .build())
                .toList();
    }

    private List<AvatarSpecieTraitDTO> toSpecieTraitDTOs(Avatar avatar) {
        return specieTraitRepository.findBySpecieOrderByTraitOrder(avatar.getSpecie()).stream()
                .filter(t -> !"SIZE".equals(t.getType()) && !"SPEED".equals(t.getType()))
                .map(t -> AvatarSpecieTraitDTO.builder()
                        .name(t.getName())
                        .description(t.getDescription())
                        .build())
                .toList();
    }

    private List<String> toFeatNames(Avatar avatar) {
        return avatarFeatRepository.findByAvatar_Id(avatar.getId()).stream()
                .map(AvatarFeat::getName)
                .toList();
    }
}

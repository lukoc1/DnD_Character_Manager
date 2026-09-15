package pl.visa.dndCM.avatar;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.gameData.background.Background;
import pl.visa.dndCM.gameData.background.BackgroundBenefit;
import pl.visa.dndCM.gameData.background.BackgroundRepository;
import pl.visa.dndCM.gameData.dndClass.DndClass;
import pl.visa.dndCM.gameData.dndClass.DndClassRepository;
import pl.visa.dndCM.exception.ErrorCode;
import pl.visa.dndCM.exception.ResourceNotFoundException;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItem;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemProperty;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemPropertyRepository;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemRepository;
import pl.visa.dndCM.gameData.feature.DndClassFeature;
import pl.visa.dndCM.gameData.feature.DndClassFeatureLevel;
import pl.visa.dndCM.gameData.feature.FeatureRepository;
import pl.visa.dndCM.gameData.specie.Specie;
import pl.visa.dndCM.gameData.specie.SpecieRepository;
import pl.visa.dndCM.gameData.specie.SpecieTraitRepository;
import pl.visa.dndCM.user.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;
    private final DndClassRepository dndClassRepository;
    private final BackgroundRepository backgroundRepository;
    private final SpecieRepository specieRepository;
    private final AvatarSkillProficiencyRepository skillProficiencyRepository;
    private final AvatarFeatRepository avatarFeatRepository;
    private final AvatarEquipmentItemRepository avatarEquipmentItemRepository;
    private final EquipmentItemRepository equipmentItemRepository;
    private final EquipmentItemPropertyRepository equipmentItemPropertyRepository;
    private final FeatureRepository featureRepository;
    private final SpecieTraitRepository specieTraitRepository;

    private static final Random RANDOM = new Random();

    /// methods

    public AvatarDTO getAvatarById(Long id) {
        return avatarRepository.findById(id)
                .map(s -> toDTO(s))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Avatar id=%s not found", id), ErrorCode.AVATAR_NOT_FOUND));
    }

    public List<AvatarDTO> findAll() {
        return avatarRepository.findAll()
                .stream().map(s -> toDTO(s)).toList();
    }

    public List<AvatarDTO> findAllByUserId(Long id) {
        return avatarRepository.findAllByUser_IdAndDraftFalse(id).stream()
                .map(a -> toDTO(a)).toList();
    }

    /** Names of the skills this avatar is proficient in - for ticking checkboxes on the sheet. */
    public Set<String> getSkillProficiencyNames(Long avatarId) {
        return skillProficiencyRepository.findByAvatar_Id(avatarId).stream()
                .map(AvatarSkillProficiency::getName)
                .collect(Collectors.toSet());
    }

    /** Ability names whose saving throw the avatar's class makes it proficient in (e.g. "Strength"). */
    public Set<String> getSavingThrowAbilities(Long avatarId) {

        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Avatar id=%s not found", avatarId), ErrorCode.AVATAR_NOT_FOUND));

        DndClass dndClass = avatar.getDndClass();
        if (dndClass == null) {
            return new HashSet<>();
        }
        return new HashSet<>(dndClass.getSavingThrowAbilities());
    }

    public Long save(AvatarDTO avatarDTO, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User %s not found", userEmail), ErrorCode.USER_NOT_FOUND));

        Avatar avatar = toEntity(avatarDTO);
        avatar.setUser(user);
        avatar.setLevel(1);
        avatar.setDraft(true);

        return avatarRepository.save(avatar).getId();
    }

    /** Admin cleanup - removes every half-finished avatar left behind in the creation wizard. */
    public void deleteAllDrafts() {

        List<Avatar> drafts = avatarRepository.findAllByDraftTrue();
        avatarRepository.deleteAll(drafts);
    }

    /** Persists the class-step choices (chosen skills + starting equipment) on an existing avatar. */
    public void saveClassStep(Long avatarId, List<String> chosenSkills, String equipmentChoice) {

        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Avatar id=%s not found", avatarId), ErrorCode.AVATAR_NOT_FOUND));

        skillProficiencyRepository.deleteByAvatar(avatar);
        if (chosenSkills != null) {
            chosenSkills.stream()
                    .map(name -> AvatarSkillProficiency.builder().avatar(avatar).name(name).build())
                    .forEach(skillProficiencyRepository::save);
        }

        DndClass dndClass = avatar.getDndClass();
        avatar.setStartingEquipmentChoice(equipmentChoice);
        String equipmentText = "B".equals(equipmentChoice) ? dndClass.getStartingEquipmentB() : dndClass.getStartingEquipmentA();
        avatar.setStartingEquipmentClass(equipmentText);

        avatarEquipmentItemRepository.deleteByAvatar(avatar);
        avatar.setGold(0);
        applyEquipmentList(avatar, equipmentText);

        avatarRepository.save(avatar);
    }

    /** Persists the six rolled ability scores, their modifiers and the base proficiency bonus. */
    public void saveAbilitiesStep(Long avatarId, int str, int dex, int con, int intel, int wis, int cha) {

        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Avatar id=%s not found", avatarId), ErrorCode.AVATAR_NOT_FOUND));

        avatar.setStrSco(str);
        avatar.setStrMod(abilityModifier(str));

        avatar.setDexSco(dex);
        avatar.setDexMod(abilityModifier(dex));

        avatar.setConsSco(con);
        avatar.setConsMod(abilityModifier(con));

        avatar.setIntSco(intel);
        avatar.setIntMod(abilityModifier(intel));

        avatar.setWisSco(wis);
        avatar.setWisMod(abilityModifier(wis));

        avatar.setCharSco(cha);
        avatar.setCharMod(abilityModifier(cha));

        avatar.setProficiencyBonus(2);

        avatarRepository.save(avatar);
    }

    /**
     * Persists the background-step choices: fixed skill proficiencies, origin feat, chosen
     * equipment package and the ability score increase. This is the last step, so the avatar
     * stops being a draft here.
     *
     * @param abilityMode  "split" -> +2 to plus2Ability and +1 to plus1Ability;
     *                     "all"   -> +1 to each of the background's three abilities
     */
    public void saveBackgroundStep(Long avatarId, String abilityMode, String plus2Ability,
                                   String plus1Ability, String equipmentChoice) {

        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Avatar id=%s not found", avatarId), ErrorCode.AVATAR_NOT_FOUND));

        Background background = avatar.getBackground();

        // fixed skill proficiencies - added on top of the ones picked in the class step
        BackgroundBenefit skillBenefit = background.getBenefit("skill_proficiency");
        if (skillBenefit != null) {
            for (String skill : splitList(skillBenefit.getDescription())) {
                skillProficiencyRepository.save(AvatarSkillProficiency.builder().avatar(avatar).name(skill).build());
            }
        }

        // origin feat
        avatarFeatRepository.deleteByAvatar(avatar);
        BackgroundBenefit featBenefit = background.getBenefit("feat");
        if (featBenefit != null) {
            avatarFeatRepository.save(AvatarFeat.builder().avatar(avatar).name(featBenefit.getDescription()).build());
        }

        // equipment package - added on top of the class equipment, plus its raw text
        String equipmentText = "B".equals(equipmentChoice) && background.getEquipmentOptionB() != null
                ? background.getEquipmentOptionB()
                : background.getEquipmentOptionA();
        if (equipmentText != null) {
            avatar.setStartingEquipmentBackground(equipmentText);
            applyEquipmentList(avatar, equipmentText);
        }

        // ability score increase
        if ("all".equals(abilityMode)) {
            background.getAbilityScoreOptions().forEach(ability -> addAbilityScore(avatar, ability, 1));
        } else {
            addAbilityScore(avatar, plus2Ability, 2);
            addAbilityScore(avatar, plus1Ability, 1);
        }

        // setting hit points
        // level 1: class hit dice + con modifier
        int maxHp = avatar.getDndClass().getHitDiceValue() + avatar.getConsMod();
        avatar.setMaxHP(maxHp);
        avatar.setCurrentHP(maxHp);
        avatar.setTempHP(0);

        avatar.setSize(avatar.getSpecie().getSize());
        avatar.setCurrentSpeed(avatar.getSpecie().getBaseSpeed());

        avatar.setDraft(false);
        avatarRepository.save(avatar);
    }

    private static int abilityModifier(int score) {
        return (int) Math.floor((score - 10) / 2.0);
    }

    private void addAbilityScore(Avatar avatar, String ability, int value) {
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

    /** "Insight and Religion" / "Insight, Religion" -> ["Insight", "Religion"]. */
    private static List<String> splitList(String text) {
        if (text == null) {
            return List.of();
        }

        return Arrays.stream(text.split(",|\\band\\b"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    /**
     * Best-effort: parses a "Greataxe, 4 Handaxes, Explorer's Pack, and 15 GP" style list and
     * adds the matched items + gold to the avatar. Unknown tokens are skipped. Same format is
     * used by class and background steps, so callers clear first if they want a fresh start.
     */
    public void applyEquipmentList(Avatar avatar, String text) {
        if (text == null) {
            return;
        }

        for (String raw : text.split(",")) {
            String token = raw.trim().replaceFirst("^and\\s+", "").trim();
            if (token.isEmpty()) {
                continue;
            }

            if (token.toUpperCase().endsWith("GP")) {
                String amount = token.substring(0, token.length() - 2).trim();
                if (!amount.isEmpty() && amount.chars().allMatch(Character::isDigit)) {
                    avatar.setGold(avatar.getGold() + Integer.parseInt(amount));
                    continue;
                }
            }

            int quantity = 1;
            String name = token;
            String[] parts = token.split(" ", 2);
            if (parts.length == 2 && !parts[0].isEmpty() && parts[0].chars().allMatch(Character::isDigit)) {
                quantity = Integer.parseInt(parts[0]);
                name = parts[1].trim();
            }

            EquipmentItem item = findEquipmentByName(name);
            if (item == null) {
                continue;
            }

            AvatarEquipmentItem row = new AvatarEquipmentItem();
            row.setAvatar(avatar);
            row.setEquipmentItem(item);
            row.setQuantity(quantity);
            avatarEquipmentItemRepository.save(row);
        }
    }

    private EquipmentItem findEquipmentByName(String name) {
        Optional<EquipmentItem> exact = equipmentItemRepository.findByNameIgnoreCase(name);
        if (exact.isPresent()) {
            return exact.get();
        }
        // "Handaxes" -> "Handaxe"
        if (name.endsWith("s")) {
            return equipmentItemRepository.findByNameIgnoreCase(name.substring(0, name.length() - 1)).orElse(null);
        }
        return null;
    }

    public void deleteAvatarById(Long id) {
        avatarRepository.deleteById(id);
    }

    /**
     * Attack bonus = ability modifier (Strength, or Dexterity for ranged/finesse weapons - whichever
     * is higher for finesse) + proficiency bonus, if the avatar's class is proficient with this weapon.
     */
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


    /// utils

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
                .classFeatures(toClassFeatureDTOs(avatar))
                .specieTraits(toSpecieTraitDTOs(avatar))
                .feats(toFeatNames(avatar))

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

    public Avatar toEntity(AvatarDTO avatarDTO) {
        return Avatar.builder()
                .id(avatarDTO.getId())
                .name(avatarDTO.getName())
                .background(findBackground(avatarDTO.getBackgroundId()))
                .dndClass(findDndClass(avatarDTO.getDndClassId()))
                .specie(findSpecie(avatarDTO.getSpecieId()))
                .subclassName(avatarDTO.getSubclassName())
                .level(avatarDTO.getLevel())
                .build();
    }

    private DndClass findDndClass(Long dndClassId) {
        if (dndClassId == null) {
            return null;
        }

        return dndClassRepository.findById(dndClassId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("DndClass id=%s not found", dndClassId), ErrorCode.DND_CLASS_NOT_FOUND));
    }

    private Background findBackground(Long backgroundId) {
        if (backgroundId == null) {
            return null;
        }

        return backgroundRepository.findById(backgroundId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Background id=%s not found", backgroundId), ErrorCode.BACKGROUND_NOT_FOUND));
    }

    private Specie findSpecie(Long specieId) {
        if (specieId == null) {
            return null;
        }

        return specieRepository.findById(specieId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Specie id=%s not found", specieId), ErrorCode.SPECIE_NOT_FOUND));
    }

    public int getPassivePerception(Long avatarId) {
        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Avatar id=%s not found", avatarId), ErrorCode.AVATAR_NOT_FOUND));

        boolean proficientInPerception = skillProficiencyRepository.findByAvatar_Id(avatarId).stream()
                .anyMatch(p -> "Perception".equals(p.getName()));

        return 10 + avatar.getWisMod() + (proficientInPerception ? avatar.getProficiencyBonus() : 0);
    }

    public void spendHitDie(Long avatarId) {
        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Avatar id=%s not found", avatarId), ErrorCode.AVATAR_NOT_FOUND));

        // hit dice amount to spent == avatar level
        if (avatar.getHitDiceSpent() >= avatar.getLevel()) {
            return;
        }

        int roll = RANDOM.nextInt(avatar.getDndClass().getHitDiceValue()) + 1
                + avatar.getConsMod();
        int heal = Math.max(roll, 0);

        int newCurrentHp = Math.min(avatar.getMaxHP(), avatar.getCurrentHP() + heal);

        avatar.setHitDiceSpent(avatar.getHitDiceSpent() + 1);
        avatar.setCurrentHP(newCurrentHp);

        avatarRepository.save(avatar);
    }


}

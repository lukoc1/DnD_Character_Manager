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
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemRepository;
import pl.visa.dndCM.gameData.specie.Specie;
import pl.visa.dndCM.gameData.specie.SpecieRepository;
import pl.visa.dndCM.user.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    private static final Random RANDOM = new Random();

    private static final Pattern GOLD = Pattern.compile("^(\\d+)\\s*GP$", Pattern.CASE_INSENSITIVE);
    private static final Pattern QUANTITY = Pattern.compile("^(\\d+)\\s+(.*)$");

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

    public Long save(AvatarDTO avatarDTO, String userName) {

        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User %s not found", userName), ErrorCode.USER_NOT_FOUND));

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

        avatar.setStrSco(str);   avatar.setStrMod(abilityModifier(str));
        avatar.setDexSco(dex);   avatar.setDexMod(abilityModifier(dex));
        avatar.setConsSco(con);  avatar.setConsMod(abilityModifier(con));
        avatar.setIntSco(intel); avatar.setIntMod(abilityModifier(intel));
        avatar.setWisSco(wis);   avatar.setWisMod(abilityModifier(wis));
        avatar.setCharSco(cha);  avatar.setCharMod(abilityModifier(cha));

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

        avatar.setDraft(false);
        avatarRepository.save(avatar);
    }

    private static int abilityModifier(int score) {
        return Math.floorDiv(score - 10, 2);
    }

    /** Adds delta to one ability score (capped at 20) and refreshes its modifier. */
    private void addAbilityScore(Avatar avatar, String ability, int delta) {
        if (ability == null) {
            return;
        }
        switch (ability) {
            case "Strength" -> { int v = Math.min(20, avatar.getStrSco() + delta); avatar.setStrSco(v); avatar.setStrMod(abilityModifier(v)); }
            case "Dexterity" -> { int v = Math.min(20, avatar.getDexSco() + delta); avatar.setDexSco(v); avatar.setDexMod(abilityModifier(v)); }
            case "Constitution" -> { int v = Math.min(20, avatar.getConsSco() + delta); avatar.setConsSco(v); avatar.setConsMod(abilityModifier(v)); }
            case "Intelligence" -> { int v = Math.min(20, avatar.getIntSco() + delta); avatar.setIntSco(v); avatar.setIntMod(abilityModifier(v)); }
            case "Wisdom" -> { int v = Math.min(20, avatar.getWisSco() + delta); avatar.setWisSco(v); avatar.setWisMod(abilityModifier(v)); }
            case "Charisma" -> { int v = Math.min(20, avatar.getCharSco() + delta); avatar.setCharSco(v); avatar.setCharMod(abilityModifier(v)); }
            default -> { /* unknown ability name - ignore */ }
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

            Matcher goldMatch = GOLD.matcher(token);
            if (goldMatch.matches()) {
                avatar.setGold(avatar.getGold() + Integer.parseInt(goldMatch.group(1)));
                continue;
            }

            int quantity = 1;
            String name = token;
            Matcher quantityMatch = QUANTITY.matcher(token);
            if (quantityMatch.matches()) {
                quantity = Integer.parseInt(quantityMatch.group(1));
                name = quantityMatch.group(2).trim();
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
                .build();
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

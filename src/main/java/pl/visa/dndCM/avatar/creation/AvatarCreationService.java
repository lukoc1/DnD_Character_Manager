package pl.visa.dndCM.avatar.creation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.avatar.Avatar;
import pl.visa.dndCM.avatar.AvatarDTO;
import pl.visa.dndCM.avatar.AvatarRepository;
import pl.visa.dndCM.avatar.AvatarService;
import pl.visa.dndCM.avatar.equipment.AvatarEquipmentItem;
import pl.visa.dndCM.avatar.feat.AvatarFeat;
import pl.visa.dndCM.avatar.proficiency.AvatarSkillProficiency;
import pl.visa.dndCM.exception.ErrorCode;
import pl.visa.dndCM.exception.ResourceNotFoundException;
import pl.visa.dndCM.gameData.background.Background;
import pl.visa.dndCM.gameData.background.BackgroundBenefit;
import pl.visa.dndCM.gameData.background.BackgroundRepository;
import pl.visa.dndCM.gameData.dndClass.DndClass;
import pl.visa.dndCM.gameData.dndClass.DndClassRepository;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItem;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemRepository;
import pl.visa.dndCM.gameData.specie.Specie;
import pl.visa.dndCM.gameData.specie.SpecieRepository;
import pl.visa.dndCM.user.User;
import pl.visa.dndCM.user.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AvatarCreationService {
    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;
    private final DndClassRepository dndClassRepository;
    private final BackgroundRepository backgroundRepository;
    private final SpecieRepository specieRepository;
    private final EquipmentItemRepository equipmentItemRepository;

    public Long save(AvatarDTO avatarDTO, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User %s not found", userEmail), ErrorCode.USER_NOT_FOUND));

        Avatar avatar = toEntity(avatarDTO);
        avatar.setUser(user);
        avatar.setLevel(1);
        avatar.setDraft(true);

        return avatarRepository.save(avatar).getId();
    }

    public void deleteAllDrafts() {
        List<Avatar> drafts = avatarRepository.findAllByDraftTrue();
        avatarRepository.deleteAll(drafts);
    }

    public void saveClassStep(Long avatarId, List<String> chosenSkills, String equipmentChoice) {

        Avatar avatar = getAvatarOrThrow(avatarId);

        avatar.getSkillProficiencies().clear();
        if (chosenSkills != null) {
            for (String name : chosenSkills) {
                avatar.getSkillProficiencies().add(AvatarSkillProficiency.builder().avatar(avatar).name(name).build());
            }
        }

        DndClass dndClass = avatar.getDndClass();
        String equipmentText = "B".equals(equipmentChoice) ? dndClass.getStartingEquipmentB() : dndClass.getStartingEquipmentA();
        avatar.setStartingEquipmentClass(equipmentText);

        avatar.getEquipmentItems().clear();
        avatar.setGold(0);
        applyEquipmentList(avatar, equipmentText);

        avatarRepository.save(avatar);
    }

    public void saveAbilitiesStep(Long avatarId, int str, int dex, int con, int intel, int wis, int cha) {

        Avatar avatar = getAvatarOrThrow(avatarId);

        avatar.setStrSco(str);
        avatar.setStrMod(AvatarService.abilityModifier(str));

        avatar.setDexSco(dex);
        avatar.setDexMod(AvatarService.abilityModifier(dex));

        avatar.setConsSco(con);
        avatar.setConsMod(AvatarService.abilityModifier(con));

        avatar.setIntSco(intel);
        avatar.setIntMod(AvatarService.abilityModifier(intel));

        avatar.setWisSco(wis);
        avatar.setWisMod(AvatarService.abilityModifier(wis));

        avatar.setCharSco(cha);
        avatar.setCharMod(AvatarService.abilityModifier(cha));

        avatar.setProficiencyBonus(2);

        avatarRepository.save(avatar);
    }


    public void saveBackgroundStep(Long avatarId, String mode, String ability1,
                                   String ability2, String equipmentChoice) {

        Avatar avatar = getAvatarOrThrow(avatarId);

        Background background = avatar.getBackground();

        // fixed skill proficiencies - added on top of the ones picked in the class step
        BackgroundBenefit skillBenefit = background.getBenefit("skill_proficiency");
        if (skillBenefit != null) {
            for (String skill : splitList(skillBenefit.getDescription())) {
                avatar.getSkillProficiencies().add(AvatarSkillProficiency.builder().avatar(avatar).name(skill).build());
            }
        }

        // origin feat
        avatar.getFeats().clear();
        BackgroundBenefit featBenefit = background.getBenefit("feat");
        if (featBenefit != null) {
            avatar.getFeats().add(AvatarFeat.builder().avatar(avatar).name(featBenefit.getDescription()).build());
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
        if ("all".equals(mode)) {
            background.getAbilityScoreOptions().forEach(ability -> AvatarService.addAbilityScore(avatar, ability, 1));
        } else {
            AvatarService.addAbilityScore(avatar, ability1, 2);
            AvatarService.addAbilityScore(avatar, ability2, 1);
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

    // "Insight and Religion" / "Insight, Religion" -> ["Insight", "Religion"]
    private static List<String> splitList(String text) {
        if (text == null) {
            return List.of();
        }

        return Arrays.stream(text.split(",|\\band\\b"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    // Parses "Greataxe, 4 Handaxes, Explorer's Pack, and 15 GP" into list and
    // adds the items + gold to the avatar - unknown are skipped.
    private void applyEquipmentList(Avatar avatar, String text) {
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
            avatar.getEquipmentItems().add(row);
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

    private Avatar toEntity(AvatarDTO avatarDTO) {
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

    private Avatar getAvatarOrThrow(Long avatarId) {
        return avatarRepository.findById(avatarId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Avatar id=%s not found", avatarId), ErrorCode.AVATAR_NOT_FOUND));
    }
}

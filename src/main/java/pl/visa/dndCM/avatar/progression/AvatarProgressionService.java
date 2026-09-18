package pl.visa.dndCM.avatar.progression;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.avatar.Avatar;
import pl.visa.dndCM.avatar.AvatarRepository;
import pl.visa.dndCM.avatar.AvatarService;
import pl.visa.dndCM.exception.ErrorCode;
import pl.visa.dndCM.exception.ResourceNotFoundException;
import pl.visa.dndCM.gameData.dndClass.DndClassLevelTableEntryRepository;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclass;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclassRepository;
import pl.visa.dndCM.gameData.feature.FeatureRepository;

import java.util.Random;

// level-up, subclass choice and ability-score-improvement (asi)
@Service
@AllArgsConstructor
public class AvatarProgressionService {
    private final AvatarRepository avatarRepository;
    private final FeatureRepository featureRepository;
    private final DndClassLevelTableEntryRepository dndClassLevelTableEntryRepository;
    private final DndSubclassRepository dndSubclassRepository;

    public boolean canGainSubclass(Long avatarId) {
        Avatar avatar = getAvatarOrThrow(avatarId);
        return (avatar.getLevel() >= 3) && (avatar.getDndsubclass() == null);
    }

    public void chooseSubclass(Long avatarId, Long subclassId) {
        Avatar avatar = getAvatarOrThrow(avatarId);

        DndSubclass subclass = dndSubclassRepository.findById(subclassId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Subclass id=%s not found", subclassId), ErrorCode.RESOURCE_NOT_FOUND));

        avatar.setDndsubclass(subclass);
        avatar.setSubclassName(subclass.getName());

        avatarRepository.save(avatar);
    }

    public boolean grantsAbilityScoreImprovementAtLevel(Long avatarId, int level) {
        Avatar avatar = getAvatarOrThrow(avatarId);

        return featureRepository.findByDndClass(avatar.getDndClass()).stream()
                .filter(f -> "Ability Score Improvement".equals(f.getName()))
                .anyMatch(f -> f.getLevelsGained().stream()
                        .anyMatch(l -> l.getLevel() == level));
    }

    public void applyAbilityScoreImprovement(Long avatarId, String mode, String ability1, String ability2) {
        Avatar avatar = getAvatarOrThrow(avatarId);

        int oldConMod = avatar.getConsMod();

        // user can choose one ability +2 or two abilities +1
        if ("single".equals(mode)) {
            AvatarService.addAbilityScore(avatar, ability1, 2);
        } else {
            AvatarService.addAbilityScore(avatar, ability1, 1);
            AvatarService.addAbilityScore(avatar, ability2, 1);
        }

        int conModGain = avatar.getConsMod() - oldConMod;
        if (conModGain > 0) {
            int hpGain = conModGain * avatar.getLevel();
            avatar.setMaxHP(avatar.getMaxHP() + hpGain);
            avatar.setCurrentHP(avatar.getCurrentHP() + hpGain);
        }

        avatarRepository.save(avatar);
    }

    public void levelUp(Long avatarId) {
        Avatar avatar = getAvatarOrThrow(avatarId);

        Random random = new Random();
        int roll = random.nextInt(avatar.getDndClass().getHitDiceValue()) + 1 + avatar.getConsMod();
        int hpGain = Math.max(roll, 1);

        avatar.setLevel(avatar.getLevel() + 1);
        avatar.setMaxHP(avatar.getMaxHP() + hpGain);
        avatar.setCurrentHP(avatar.getCurrentHP() + hpGain);

        // checks entry table for given class and fixes proficiency bonus based on level
        dndClassLevelTableEntryRepository.findByDndClassAndColumnName(avatar.getDndClass(), "Proficiency Bonus").stream()
                .filter(entry -> entry.getLevel() == avatar.getLevel())
                .findFirst()
                .ifPresent(entry -> avatar.setProficiencyBonus(parseBonus(entry.getValue())));

        avatarRepository.save(avatar);
    }

    // "+2" -> "2"
    private static int parseBonus(String value) {
        return Integer.parseInt(value.replace("+", "").trim());
    }

    private Avatar getAvatarOrThrow(Long avatarId) {
        return avatarRepository.findById(avatarId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Avatar id=%s not found", avatarId), ErrorCode.AVATAR_NOT_FOUND));
    }
}

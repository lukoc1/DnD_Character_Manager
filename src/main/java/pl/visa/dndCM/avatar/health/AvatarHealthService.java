package pl.visa.dndCM.avatar.health;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.avatar.Avatar;
import pl.visa.dndCM.avatar.AvatarRepository;
import pl.visa.dndCM.exception.ErrorCode;
import pl.visa.dndCM.exception.ResourceNotFoundException;

import java.util.Random;

// HP/temp-HP/hit-dice changes on existing avatar
@Service
@AllArgsConstructor
public class AvatarHealthService {
    private final AvatarRepository avatarRepository;

    public void takeDamage(Long avatarId, int amount) {
        Avatar avatar = getAvatarOrThrow(avatarId);

        int remainingDamage = amount - avatar.getTempHP();

        if (remainingDamage > 0) {
            avatar.setTempHP(0);
            avatar.setCurrentHP(Math.max(0, avatar.getCurrentHP() - remainingDamage));
        } else {
            avatar.setTempHP(avatar.getTempHP() - amount);
        }

        avatarRepository.save(avatar);
    }

    public void heal(Long avatarId, int amount) {
        Avatar avatar = getAvatarOrThrow(avatarId);

        int newCurrentHp = Math.min(avatar.getMaxHP(), avatar.getCurrentHP() + amount);
        avatar.setCurrentHP(newCurrentHp);

        avatarRepository.save(avatar);
    }

    public void addTempHp(Long avatarId, int amount) {
        Avatar avatar = getAvatarOrThrow(avatarId);

        avatar.setTempHP(amount);
        avatarRepository.save(avatar);
    }

    public void spendHitDie(Long avatarId) {
        Avatar avatar = getAvatarOrThrow(avatarId);

        // hit dice amount to spent == avatar level
        if (avatar.getHitDiceSpent() >= avatar.getLevel()) {
            return;
        }

        Random random = new Random();
        int roll = random.nextInt(avatar.getDndClass().getHitDiceValue()) + 1
                + avatar.getConsMod();
        int heal = Math.max(roll, 0);

        int newCurrentHp = Math.min(avatar.getMaxHP(), avatar.getCurrentHP() + heal);

        avatar.setHitDiceSpent(avatar.getHitDiceSpent() + 1);
        avatar.setCurrentHP(newCurrentHp);

        avatarRepository.save(avatar);
    }

    private Avatar getAvatarOrThrow(Long avatarId) {
        return avatarRepository.findById(avatarId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Avatar id=%s not found", avatarId), ErrorCode.AVATAR_NOT_FOUND));
    }
}

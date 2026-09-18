package pl.visa.dndCM.avatar.inventory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.avatar.Avatar;
import pl.visa.dndCM.avatar.AvatarRepository;
import pl.visa.dndCM.avatar.equipment.AvatarEquipmentItem;
import pl.visa.dndCM.avatar.equipment.AvatarEquipmentItemRepository;
import pl.visa.dndCM.exception.ErrorCode;
import pl.visa.dndCM.exception.ResourceNotFoundException;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItem;
import pl.visa.dndCM.gameData.equipmentItem.EquipmentItemRepository;

import java.util.List;

// equipment changes on existing avatar
@Service
@AllArgsConstructor
public class AvatarInventoryService {
    private final AvatarRepository avatarRepository;
    private final AvatarEquipmentItemRepository avatarEquipmentItemRepository;
    private final EquipmentItemRepository equipmentItemRepository;

    public List<String> getAllEquipmentItemNames() {
        return equipmentItemRepository.findAll().stream()
                .map(EquipmentItem::getName)
                .sorted()
                .toList();
    }

    public void addCoins(Long avatarId, int amount) {
        Avatar avatar = getAvatarOrThrow(avatarId);

        avatar.setGold(avatar.getGold() + amount);
        avatarRepository.save(avatar);
    }

    public boolean loseCoins(Long avatarId, int amount) {
        Avatar avatar = getAvatarOrThrow(avatarId);

        if (avatar.getGold() < amount) {
            return false;
        }

        avatar.setGold(avatar.getGold() - amount);
        avatarRepository.save(avatar);
        return true;
    }

    public void addItem(Long avatarId, String itemName) {
        Avatar avatar = getAvatarOrThrow(avatarId);

        EquipmentItem item = equipmentItemRepository.findByNameIgnoreCase(itemName)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Equipment item '%s' not found", itemName), ErrorCode.EQUIPMENT_ITEM_NOT_FOUND));

        AvatarEquipmentItem avatarItem = avatarEquipmentItemRepository
                .findByAvatar_IdAndEquipmentItem_Id(avatarId, item.getId())
                .orElse(null);

        if (avatarItem == null) {
            avatarItem = new AvatarEquipmentItem();
            avatarItem.setAvatar(avatar);
            avatarItem.setEquipmentItem(item);
            avatarItem.setQuantity(0);
        }

        avatarItem.setQuantity(avatarItem.getQuantity() + 1);
        avatarEquipmentItemRepository.save(avatarItem);
    }

    public void loseItem(Long avatarId, String itemName) {
        EquipmentItem item = equipmentItemRepository.findByNameIgnoreCase(itemName)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Equipment item '%s' not found", itemName), ErrorCode.EQUIPMENT_ITEM_NOT_FOUND));

        AvatarEquipmentItem avatarItem = avatarEquipmentItemRepository.findByAvatar_IdAndEquipmentItem_Id(avatarId, item.getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Avatar id=%s does not have item '%s'", avatarId, itemName), ErrorCode.EQUIPMENT_ITEM_NOT_FOUND));

        if (avatarItem.getQuantity() <= 1) {
            avatarEquipmentItemRepository.delete(avatarItem);
        } else {
            avatarItem.setQuantity(avatarItem.getQuantity() - 1);
            avatarEquipmentItemRepository.save(avatarItem);
        }
    }

    private Avatar getAvatarOrThrow(Long avatarId) {
        return avatarRepository.findById(avatarId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Avatar id=%s not found", avatarId), ErrorCode.AVATAR_NOT_FOUND));
    }
}

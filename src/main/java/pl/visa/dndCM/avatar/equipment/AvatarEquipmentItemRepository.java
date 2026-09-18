package pl.visa.dndCM.avatar.equipment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.visa.dndCM.avatar.Avatar;

import java.util.Optional;

@Repository
public interface AvatarEquipmentItemRepository extends JpaRepository<AvatarEquipmentItem, Long> {

    void deleteByAvatar(Avatar avatar);

    Optional<AvatarEquipmentItem> findByAvatar_IdAndEquipmentItem_Id(Long avatarId, Long equipmentItemId);
}

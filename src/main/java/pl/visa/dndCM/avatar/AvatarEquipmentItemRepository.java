package pl.visa.dndCM.avatar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarEquipmentItemRepository extends JpaRepository<AvatarEquipmentItem, Long> {

    void deleteByAvatar(Avatar avatar);
}

package pl.visa.dndCM.gameData.equipmentItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentItemPropertyRepository extends JpaRepository<EquipmentItemProperty, Long> {

    boolean existsByEquipmentItem(EquipmentItem equipmentItem);

    List<EquipmentItemProperty> findByEquipmentItem(EquipmentItem equipmentItem);
}

package pl.visa.dndCM.equipmentItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeaponRepository extends JpaRepository<EquipmentItem, Long> {
}

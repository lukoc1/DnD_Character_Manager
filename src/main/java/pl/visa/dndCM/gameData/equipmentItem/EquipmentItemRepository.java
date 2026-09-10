package pl.visa.dndCM.gameData.equipmentItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentItemRepository extends JpaRepository<EquipmentItem, Long> {
    boolean existsByApiIndex(String apiIndex);

    Optional<EquipmentItem> findByApiIndex(String apiIndex);

    Optional<EquipmentItem> findByNameIgnoreCase(String name);

    List<EquipmentItem> findByCategory(String category);
}

package pl.visa.dndCM.gameData.equipmentItem.damageType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DamageTypeRepository extends JpaRepository<DamageType, Long> {

    Optional<DamageType> findByApiIndex(String apiIndex);
}

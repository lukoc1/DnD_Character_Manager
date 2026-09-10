package pl.visa.dndCM.gameData.equipmentItem.damageType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.http.HttpHeaders;
import java.util.List;

@Repository
public interface DamageTypeRepository extends JpaRepository<DamageType, Long> {

    boolean existsByApiIndex(String apiIndex);

    List<DamageType> findByApiIndex(String apiIndex);
}

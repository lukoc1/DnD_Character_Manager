package pl.visa.dndCM.equipmentItem.damageType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.http.HttpHeaders;
import java.util.List;

@Repository
public interface DamageTypeRepository extends JpaRepository<DamageType, Long> {


    List<DamageType> findByApiIndex(String apiIndex);

    boolean existsByApiIndex(String apiIndex);
}

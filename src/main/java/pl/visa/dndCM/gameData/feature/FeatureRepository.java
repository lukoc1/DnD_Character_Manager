package pl.visa.dndCM.gameData.feature;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeatureRepository extends JpaRepository<DndClassFeature, Long> {

    boolean existsByApiIndex(String apiIndex);

    Optional<DndClassFeature> findByApiIndex(String apiIndex);
}

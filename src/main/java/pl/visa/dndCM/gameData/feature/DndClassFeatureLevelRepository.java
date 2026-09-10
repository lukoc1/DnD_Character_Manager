package pl.visa.dndCM.gameData.feature;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DndClassFeatureLevelRepository extends JpaRepository<DndClassFeatureLevel, Long> {

    boolean existsByFeature(DndClassFeature feature);
}

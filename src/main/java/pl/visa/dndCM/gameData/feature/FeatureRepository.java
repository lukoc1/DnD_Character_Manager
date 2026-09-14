package pl.visa.dndCM.gameData.feature;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.visa.dndCM.gameData.dndClass.DndClass;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclass;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeatureRepository extends JpaRepository<DndClassFeature, Long> {

    boolean existsByApiIndex(String apiIndex);

    Optional<DndClassFeature> findByApiIndex(String apiIndex);

    List<DndClassFeature> findByDndClass(DndClass dndClass);

    List<DndClassFeature> findBySubclass(DndSubclass subclass);
}

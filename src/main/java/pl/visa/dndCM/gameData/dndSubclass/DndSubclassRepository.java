package pl.visa.dndCM.gameData.dndSubclass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DndSubclassRepository extends JpaRepository<DndSubclass, Long> {

    boolean existsByApiIndex(String apiIndex);

    Optional<DndSubclass> findByApiIndex(String apiIndex);

    List<DndSubclass> findByDndClass_Id(Long dndClassId);

    List<DndSubclass> findByDndClass_ApiIndex(String apiIndex);
}

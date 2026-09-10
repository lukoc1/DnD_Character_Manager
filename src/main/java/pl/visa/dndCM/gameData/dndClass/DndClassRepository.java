package pl.visa.dndCM.gameData.dndClass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DndClassRepository extends JpaRepository<DndClass, Long> {
    boolean existsByApiIndex(String apiIndex);

    Optional<DndClass> findByApiIndex(String apiIndex);
}

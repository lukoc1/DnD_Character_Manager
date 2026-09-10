package pl.visa.dndCM.gameData.dndClass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DndClassSkillOptionRepository extends JpaRepository<DndClassSkillOption, Long> {

    boolean existsByDndClass(DndClass dndClass);
}

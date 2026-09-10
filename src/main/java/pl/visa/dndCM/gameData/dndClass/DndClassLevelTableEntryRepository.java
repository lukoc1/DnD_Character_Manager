package pl.visa.dndCM.gameData.dndClass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DndClassLevelTableEntryRepository extends JpaRepository<DndClassLevelTableEntry, Long> {

    List<DndClassLevelTableEntry> findByDndClass(DndClass dndClass);

    List<DndClassLevelTableEntry> findByDndClassAndColumnName(DndClass dndClass, String columnName);

    boolean existsByDndClass(DndClass dndClass);

    boolean existsByDndClassAndColumnName(DndClass dndClass, String columnName);
}

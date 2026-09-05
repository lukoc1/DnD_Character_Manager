package pl.visa.dndCM.avatar.dndClass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.http.HttpHeaders;

@Repository
public interface DndClassRepository extends JpaRepository<DndClass, Long> {
    boolean existsByApiIndex(String apiIndex);
}

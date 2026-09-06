package pl.visa.dndCM.gameData.background;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.JsonNode;

@Repository
public interface BackgroundRepository extends JpaRepository<Background, Long> {
    boolean existsByApiIndex(String apiIndex);
}

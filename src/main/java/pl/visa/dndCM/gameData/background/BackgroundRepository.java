package pl.visa.dndCM.gameData.background;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BackgroundRepository extends JpaRepository<Background, Long> {
    boolean existsByApiIndex(String apiIndex);

    Optional<Background> findByApiIndex(String apiIndex);
}

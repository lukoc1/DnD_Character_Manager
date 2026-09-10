package pl.visa.dndCM.gameData.background;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackgroundBenefitRepository extends JpaRepository<BackgroundBenefit, Long> {

    boolean existsByBackground(Background background);
}

package pl.visa.dndCM.gameData.specie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecieRepository extends JpaRepository<Specie, Long> {
    boolean existsByApiIndex(String apiIndex);

    Optional<Specie> findByApiIndex(String apiIndex);
}

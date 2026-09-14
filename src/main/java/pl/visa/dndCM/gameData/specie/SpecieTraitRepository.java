package pl.visa.dndCM.gameData.specie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecieTraitRepository extends JpaRepository<SpecieTrait, Long> {

    boolean existsBySpecie(Specie specie);

    @Query("SELECT t FROM SpecieTrait t WHERE t.specie.id = :specieId AND t.type = :type")
    Optional<SpecieTrait> findBySpecieIdAndType(@Param("specieId") Long specieId, @Param("type") String type);
}

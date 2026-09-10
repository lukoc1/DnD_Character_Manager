package pl.visa.dndCM.gameData.specie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecieTraitRepository extends JpaRepository<SpecieTrait, Long> {

    boolean existsBySpecie(Specie specie);
}

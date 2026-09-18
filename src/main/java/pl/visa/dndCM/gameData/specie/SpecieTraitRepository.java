package pl.visa.dndCM.gameData.specie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecieTraitRepository extends JpaRepository<SpecieTrait, Long> {

    boolean existsBySpecie(Specie specie);

    List<SpecieTrait> findBySpecieOrderByTraitOrder(Specie specie);
}

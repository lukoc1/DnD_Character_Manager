package pl.visa.dndCM.gameData.specie;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.open5eApi.ApiClient;
import pl.visa.dndCM.open5eApi.specie.ApiSpecieDTO;
import pl.visa.dndCM.open5eApi.specie.ApiTraitDTO;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class Open5eSpecieImporter {

    private final ApiClient apiClient;
    private final SpecieRepository specieRepository;
    private final SpecieTraitRepository specieTraitRepository;

    public void importSpecies() {

        List<ApiSpecieDTO> all = apiClient.getSpecies().getResults();

        all.stream()
                .filter(s -> s.getSubspeciesOf() == null)
                .forEach(this::importSpecie);

        all.stream()
                .filter(s -> s.getSubspeciesOf() != null)
                .forEach(this::importSubspecie);
    }

    private void importSpecie(ApiSpecieDTO dto) {

        Specie specie = specieRepository.findByApiIndex(dto.getKey()).orElseGet(Specie::new);
        specie.setApiIndex(dto.getKey());
        specie.setName(dto.getName());

        Specie saved = specieRepository.save(specie);

        importTraits(dto.getTraits(), saved);
    }

    private void importSubspecie(ApiSpecieDTO dto) {

        Optional<Specie> parent = specieRepository.findByApiIndex(dto.getSubspeciesOf().getKey());
        if (parent.isEmpty()) {
            return;
        }

        Specie subspecie = specieRepository.findByApiIndex(dto.getKey()).orElseGet(Specie::new);
        subspecie.setApiIndex(dto.getKey());
        subspecie.setName(dto.getName());
        subspecie.setParentSpecie(parent.get());

        Specie saved = specieRepository.save(subspecie);

        importTraits(dto.getTraits(), saved);
    }

    private void importTraits(List<ApiTraitDTO> traits, Specie specie) {
        if (traits == null || specieTraitRepository.existsBySpecie(specie)) {
            return;
        }

        List<SpecieTrait> rows = traits.stream()
                .map(trait -> SpecieTrait.builder()
                        .specie(specie)
                        .name(trait.getName())
                        .description(trait.getDesc())
                        .type(trait.getType())
                        .traitOrder(trait.getOrder())
                        .build())
                .toList();

        specieTraitRepository.saveAll(rows);
    }
}

package pl.visa.dndCM.gameData.specie;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.open5eApi.ApiClient;
import pl.visa.dndCM.open5eApi.specie.ApiSpecieDTO;
import pl.visa.dndCM.open5eApi.specie.ApiTraitDTO;

import java.util.List;

@Service
@AllArgsConstructor
public class Open5eSpecieImporter {

    private final ApiClient apiClient;
    private final SpecieRepository specieRepository;
    private final SpecieTraitRepository specieTraitRepository;

    public void importSpecies() {

        List<ApiSpecieDTO> specieDTOList = apiClient.getSpecies().getResults();

        specieDTOList.forEach(this::importSpecie);
    }

    private void importSpecie(ApiSpecieDTO dto) {

        Specie specie = specieRepository.findByApiIndex(dto.getKey()).orElseGet(Specie::new);
        specie.setApiIndex(dto.getKey());
        specie.setName(dto.getName());

        String speed = traitFirstWord(dto.getTraits(), "SPEED");

        specie.setSize(traitFirstWord(dto.getTraits(), "SIZE"));
        specie.setBaseSpeed(speed == null ? 0 : Integer.parseInt(speed));

        Specie saved = specieRepository.save(specie);

        importTraits(dto.getTraits(), saved);
    }

    private String traitFirstWord(List<ApiTraitDTO> traits, String type) {
        String desc = findTraitDesc(traits, type);
        if (desc == null || desc.isBlank()) {
            return null;
        }

        return desc.trim().split(" ")[0];
    }

    private String findTraitDesc(List<ApiTraitDTO> traits, String type) {
        if (traits == null) {
            return null;
        }

        return traits.stream()
                .filter(t -> type.equals(t.getType()))
                .map(t -> t.getDesc())
                .findFirst()
                .orElse(null);
    }

    private void importTraits(List<ApiTraitDTO> traits, Specie specie) {
        if (traits == null || specieTraitRepository.existsBySpecie(specie)) {
            return;
        }

        List<SpecieTrait> rows = traits.stream()
                .map(t -> SpecieTrait.builder()
                        .specie(specie)
                        .name(t.getName())
                        .description(t.getDesc())
                        .type(t.getType())
                        .traitOrder(t.getOrder())
                        .build())
                .toList();

        specieTraitRepository.saveAll(rows);
    }
}

package pl.visa.dndCM.gameData.specie;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.dnd5eapi.ApiClient;
import pl.visa.dndCM.dnd5eapi.ApiListDTO;
import pl.visa.dndCM.dnd5eapi.ApiReferenceDTO;

@Service
@AllArgsConstructor
public class SpecieImporter {

    private final ApiClient apiClient;
    private final SpecieRepository specieRepository;

    public void importSpecies() {

        ApiListDTO data = apiClient.getSpecies();

        data.getResults().stream()
                .filter(d -> !specieRepository.existsByApiIndex(d.getIndex()))
                .map(d -> toEntity(d))
                .forEach(d -> specieRepository.save(d));

    }

    private Specie toEntity(ApiReferenceDTO ref) {

        Specie specie = new Specie();
        specie.setApiIndex(ref.getIndex());
        specie.setName(ref.getName());

        return specie;
    }
}

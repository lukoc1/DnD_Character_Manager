package pl.visa.dndCM.gameData.dndClass;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.dnd5eapi.ApiClient;
import pl.visa.dndCM.dnd5eapi.ApiListDTO;
import pl.visa.dndCM.dnd5eapi.ApiReferenceDTO;

@Service
@AllArgsConstructor
public class DndClassImporter {

    private final ApiClient apiClient;
    private final DndClassRepository dndClassRepository;

    public void importDndClasses() {

        ApiListDTO data = apiClient.getDndClasses();

        data.getResults().stream()
                .filter(d -> !dndClassRepository.existsByApiIndex(d.getIndex()))
                .map(d -> toEntity(d))
                .forEach(d -> dndClassRepository.save(d));

    }

    private DndClass toEntity(ApiReferenceDTO ref) {

        DndClass dndClass = new DndClass();
        dndClass.setApiIndex(ref.getIndex());
        dndClass.setName(ref.getName());

        return dndClass;
    }
}

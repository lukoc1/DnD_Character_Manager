package pl.visa.dndCM.gameData.dndClass;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.dnd5eapi.ApiClient;
import pl.visa.dndCM.dnd5eapi.ApiListDTO;

@Service
@AllArgsConstructor
public class DndClassLoader {

    private final ApiClient apiClient;
    private final DndClassRepository dndClassRepository;

    public void loadDndClasses() {

        ApiListDTO data = apiClient.getDndClasses();

        data.getResults().stream()
                .filter(d -> !dndClassRepository.existsByApiIndex(d.getIndex()))
                .map(d -> {
                    DndClass dndClass = new DndClass();
                    dndClass.setApiIndex(d.getIndex());
                    dndClass.setName(d.getName());

                    return dndClass;
                }).forEach(d -> dndClassRepository.save(d));

    }
}

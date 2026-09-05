package pl.visa.dndCM.apiLoader;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.avatar.dndClass.DndClass;
import pl.visa.dndCM.avatar.dndClass.DndClassListDTO;
import pl.visa.dndCM.avatar.dndClass.DndClassRepository;

@Service
@AllArgsConstructor
public class DndClassLoader {

    private final ApiClient apiClient;
    private final DndClassRepository dndClassRepository;

    public void loadDndClasses() {

        DndClassListDTO data = apiClient.getDndClasses();

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

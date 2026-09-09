package pl.visa.dndCM.gameData.dndSubclass;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.dnd5eapi.ApiClient;
import pl.visa.dndCM.gameData.dndClass.DndClass;
import pl.visa.dndCM.gameData.dndClass.DndClassRepository;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DndSubclassImporter {

    private final ApiClient apiClient;
    private final DndSubclassRepository dndSubclassRepository;
    private final DndClassRepository dndClassRepository;

    public void importDndSubclasses() {

        apiClient.getDndSubclasses().getResults().stream()
                .filter(d -> !dndSubclassRepository.existsByApiIndex(d.getIndex()))
                .map(d -> toEntity(d.getIndex()))
                .filter(Objects::nonNull)
                .forEach(dndSubclassRepository::save);
    }

    private DndSubclass toEntity(String index) {

        DndSubclassDTO detail = apiClient.getDndSubclassDetail(index);
        if (detail == null) {
            return null;
        }

        DndSubclass subclass = new DndSubclass();
        subclass.setApiIndex(detail.getIndex());
        subclass.setName(detail.getName());
        resolveDndClass(detail).ifPresent(subclass::setDndClass);

        return subclass;
    }

    private Optional<DndClass> resolveDndClass(DndSubclassDTO detail) {
        if (detail.getDndClass() == null) {
            return Optional.empty();
        }
        return dndClassRepository.findByApiIndex(detail.getDndClass().getIndex());
    }
}

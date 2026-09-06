package pl.visa.dndCM.gameData.background;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.dnd5eapi.ApiClient;
import pl.visa.dndCM.dnd5eapi.ApiListDTO;
import pl.visa.dndCM.dnd5eapi.ApiReferenceDTO;
import pl.visa.dndCM.gameData.dndClass.DndClass;
import pl.visa.dndCM.gameData.dndClass.DndClassRepository;

//TODO
//DALEJ TO

@Service
@AllArgsConstructor
public class BackgroundImporter {

    private final ApiClient apiClient;
    private final BackgroundRepository backgroundRepository;

    public void importBackgrounds() {

        ApiListDTO data = apiClient.getBackgrounds();

        data.getResults().stream()
                .filter(d -> !backgroundRepository.existsByApiIndex(d.getIndex()))
                .map(d -> toEntity(d))
                .forEach(d -> backgroundRepository.save(d));

    }

    private Background toEntity(ApiReferenceDTO ref) {

        Background background = new Background();
        background.setApiIndex(ref.getIndex());
        background.setName(ref.getName());

        return background;
    }
}

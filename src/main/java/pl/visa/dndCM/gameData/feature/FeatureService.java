package pl.visa.dndCM.gameData.feature;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FeatureService {

    private final FeatureRepository featureRepository;

    public List<DndClassFeature> findAll() {
        return featureRepository.findAll();
    }

}

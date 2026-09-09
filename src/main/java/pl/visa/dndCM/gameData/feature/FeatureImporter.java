package pl.visa.dndCM.gameData.feature;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.dnd5eapi.ApiClient;
import pl.visa.dndCM.dnd5eapi.ApiReferenceDTO;
import pl.visa.dndCM.gameData.dndClass.DndClass;
import pl.visa.dndCM.gameData.dndClass.DndClassRepository;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclass;
import pl.visa.dndCM.gameData.dndSubclass.DndSubclassRepository;

import java.util.Objects;
import java.util.Optional;

/**
 * Import cech z {@code /api/2024/features/}. Lista daje tylko {@code {index, name, url}},
 * więc {@code toEntity} pobiera szczegół każdej cechy ({@code /features/{index}}) —
 * jak {@code EquipmentItemImporter}. Cecha z wypełnionym {@code subclass} → cecha
 * podklasy, bez → cecha klasy. Wymaga wcześniejszego importu klas i podklas.
 */
@Service
@AllArgsConstructor
public class FeatureImporter {

    private final ApiClient apiClient;
    private final FeatureRepository featureRepository;
    private final DndClassRepository dndClassRepository;
    private final DndSubclassRepository dndSubclassRepository;

    public void importFeatures() {

        apiClient.getFeatures().getResults().stream()
                .filter(d -> !featureRepository.existsByApiIndex(d.getIndex()))
                .map(d -> toEntity(d.getIndex()))
                .filter(Objects::nonNull)
                .forEach(featureRepository::save);
    }

    private DndClassFeature toEntity(String index) {

        FeatureDTO detail = apiClient.getFeatureDetail(index);
        if (detail == null) {
            return null;
        }

        DndClassFeature feature = new DndClassFeature();
        feature.setApiIndex(detail.getIndex());
        feature.setName(detail.getName());
        feature.setDescription(detail.getDescription());
        feature.setLevel(parseLevel(detail.getLevel()));
        resolveDndClass(detail).ifPresent(feature::setDndClass);
        resolveSubclass(detail).ifPresent(feature::setSubclass);

        return feature;
    }

    private Optional<DndClass> resolveDndClass(FeatureDTO detail) {
        if (detail.getDndClass() == null) {
            return Optional.empty();
        }
        return dndClassRepository.findByApiIndex(detail.getDndClass().getIndex());
    }

    private Optional<DndSubclass> resolveSubclass(FeatureDTO detail) {
        if (detail.getSubclass() == null) {
            return Optional.empty();
        }
        return dndSubclassRepository.findByApiIndex(detail.getSubclass().getIndex());
    }

    /** "barbarian-6" -> 6; "Barbarian 6" jako zapas; przy błędzie 0. */
    private int parseLevel(ApiReferenceDTO levelRef) {
        if (levelRef == null) {
            return 0;
        }
        String source = levelRef.getIndex() != null ? levelRef.getIndex() : levelRef.getName();
        if (source == null) {
            return 0;
        }
        String tail = source.replaceAll(".*[^0-9](\\d+)$", "$1");
        try {
            return Integer.parseInt(tail);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

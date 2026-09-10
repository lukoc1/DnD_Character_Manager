package pl.visa.dndCM.gameData.background;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.open5eApi.ApiClient;
import pl.visa.dndCM.open5eApi.background.ApiBackgroundDTO;
import pl.visa.dndCM.open5eApi.background.ApiBenefitDTO;

import java.util.List;

@Service
@AllArgsConstructor
public class Open5eBackgroundImporter {

    private final ApiClient apiClient;
    private final BackgroundRepository backgroundRepository;
    private final BackgroundBenefitRepository backgroundBenefitRepository;

    public void importBackgrounds() {

        List<ApiBackgroundDTO> backgrounds = apiClient.getBackgrounds().getResults();

        backgrounds.forEach(this::importBackground);
    }

    private void importBackground(ApiBackgroundDTO dto) {

        Background background = backgroundRepository.findByApiIndex(dto.getKey()).orElseGet(Background::new);
        background.setApiIndex(dto.getKey());
        background.setName(dto.getName());

        Background saved = backgroundRepository.save(background);

        if (backgroundBenefitRepository.existsByBackground(saved) || dto.getBenefits() == null) {
            return;
        }

        List<BackgroundBenefit> benefits = dto.getBenefits().stream()
                .map(b -> toBenefit(b, saved))
                .toList();

        backgroundBenefitRepository.saveAll(benefits);
    }

    private BackgroundBenefit toBenefit(ApiBenefitDTO dto, Background background) {
        return BackgroundBenefit.builder()
                .background(background)
                .type(dto.getType())
                .name(dto.getName())
                .description(dto.getDesc())
                .build();
    }
}

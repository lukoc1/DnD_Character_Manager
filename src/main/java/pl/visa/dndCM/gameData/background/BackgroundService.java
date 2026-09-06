package pl.visa.dndCM.gameData.background;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BackgroundService {

    private final BackgroundRepository backgroundRepository;

    public List<Background> findAll() {
        return backgroundRepository.findAll();
    }
}

package pl.visa.dndCM.gameData.background;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.exception.ErrorCode;
import pl.visa.dndCM.exception.ResourceNotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class BackgroundService {

    private final BackgroundRepository backgroundRepository;

    public List<Background> findAll() {
        return backgroundRepository.findAll();
    }

    public Background findById(Long id) {
        return backgroundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Background id=%s not found", id), ErrorCode.BACKGROUND_NOT_FOUND));
    }
}

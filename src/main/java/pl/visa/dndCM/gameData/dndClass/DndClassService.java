package pl.visa.dndCM.gameData.dndClass;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DndClassService {

    private final DndClassRepository dndClassRepository;

    public DndClassService(DndClassRepository dndClassRepository) {
        this.dndClassRepository = dndClassRepository;
    }

    public List<DndClass> findAll() {
        return dndClassRepository.findAll();
    }

    public DndClass findById(Long id) {
        return dndClassRepository.findById(id).orElseThrow();
    }

}

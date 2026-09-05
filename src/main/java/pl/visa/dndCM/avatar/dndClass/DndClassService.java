package pl.visa.dndCM.avatar.dndClass;

import org.springframework.stereotype.Service;

@Service
public class DndClassService {

    private final DndClassRepository dndClassRepository;

    public DndClassService(DndClassRepository dndClassRepository) {
        this.dndClassRepository = dndClassRepository;
    }

}

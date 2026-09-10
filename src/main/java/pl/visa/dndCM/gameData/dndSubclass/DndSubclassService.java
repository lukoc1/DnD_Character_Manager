package pl.visa.dndCM.gameData.dndSubclass;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DndSubclassService {

    private final DndSubclassRepository dndSubclassRepository;

    public List<DndSubclass> findAll() {
        return dndSubclassRepository.findAll();
    }

    public List<DndSubclass> findByClassId(Long dndClassId) {
        return dndSubclassRepository.findByDndClass_Id(dndClassId);
    }

    public List<DndSubclass> findByClassApiIndex(String apiIndex) {
        return dndSubclassRepository.findByDndClass_ApiIndex(apiIndex);
    }
}

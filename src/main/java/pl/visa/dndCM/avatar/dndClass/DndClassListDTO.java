package pl.visa.dndCM.avatar.dndClass;

import lombok.Getter;
import lombok.Setter;
import pl.visa.dndCM.equipmentItem.damageType.DamageTypeDTO;

import java.util.List;

@Getter
@Setter
public class DndClassListDTO {

    private int count;
    private List<DamageTypeDTO> results;
}

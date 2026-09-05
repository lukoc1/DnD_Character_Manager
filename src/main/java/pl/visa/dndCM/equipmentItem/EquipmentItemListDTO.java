package pl.visa.dndCM.equipmentItem;

import lombok.Getter;
import lombok.Setter;
import pl.visa.dndCM.equipmentItem.damageType.DamageTypeDTO;

import java.util.List;

@Getter
@Setter
public class EquipmentItemListDTO {

    private int count;
    private List<DamageTypeDTO> results;
}

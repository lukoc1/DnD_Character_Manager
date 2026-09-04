package pl.visa.dndCM.equipmentItem.damageType;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DamageTypeListDTO {

    private int count;
    private List<DamageTypeDTO> results;
}

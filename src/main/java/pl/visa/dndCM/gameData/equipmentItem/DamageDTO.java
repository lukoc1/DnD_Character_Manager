package pl.visa.dndCM.gameData.equipmentItem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import pl.visa.dndCM.dnd5eapi.ApiReferenceDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DamageDTO {

    @JsonProperty("damage_dice")
    private String damageDice;

    @JsonProperty("damage_type")
    private ApiReferenceDTO damageType;
}

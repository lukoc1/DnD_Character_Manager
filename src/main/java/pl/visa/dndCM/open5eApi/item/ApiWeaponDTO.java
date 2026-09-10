package pl.visa.dndCM.open5eApi.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/** An item's "weapon" - set only for weapons. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiWeaponDTO {

    @JsonProperty("damage_type")
    private ApiNamedKeyDTO damageType;

    @JsonProperty("damage_dice")
    private String damageDice;

    private List<ApiPropertyEntryDTO> properties;

    @JsonProperty("is_simple")
    private boolean isSimple;

    @JsonProperty("is_martial")
    private boolean isMartial;

    @JsonProperty("is_improvised")
    private boolean isImprovised;

    @JsonProperty("distance_unit")
    private String distanceUnit;
}

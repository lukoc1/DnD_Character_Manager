package pl.visa.dndCM.open5eApi.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Response from /items.
 * "weapon" and "armor" are null unless the item is a weapon / armor.
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiItemDTO {

    private String key;
    private String name;
    private String desc;
    private ApiNamedKeyDTO category;
    private ApiWeaponDTO weapon;
    private ApiArmorDTO armor;
    private ApiNamedKeyDTO size;

    private String weight;

    @JsonProperty("weight_unit")
    private String weightUnit;

    private String cost;
}

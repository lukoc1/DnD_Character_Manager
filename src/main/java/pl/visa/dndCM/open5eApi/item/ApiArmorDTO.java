package pl.visa.dndCM.open5eApi.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/** An item's "armor" stats - for armor and shield */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiArmorDTO {

    // "light" / "medium" / "heavy" / "shield"
    private String category;

    @JsonProperty("ac_base")
    private Integer acBase;

    @JsonProperty("ac_display")
    private String acDisplay;

    @JsonProperty("ac_add_dexmod")
    private boolean acAddDexmod;

    @JsonProperty("ac_cap_dexmod")
    private Integer acCapDexmod;

    @JsonProperty("grants_stealth_disadvantage")
    private boolean grantsStealthDisadvantage;

    @JsonProperty("strength_score_required")
    private Integer strengthScoreRequired;
}

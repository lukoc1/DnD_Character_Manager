package pl.visa.dndCM.gameData.equipmentItem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import pl.visa.dndCM.dnd5eapi.ApiReferenceDTO;

import java.util.List;

/**
 * Odpowiedź z {@code /equipment/{index}} na dnd5eapi.co – tylko surowe pola.
 * Mapowanie na encję robi {@link EquipmentItemLoader}, a kategorię wylicza
 * {@link EquipmentCategory#fromApiCategories(List)}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentItemDTO {

    private String index;
    private String name;

    @JsonProperty("equipment_categories")
    private List<ApiReferenceDTO> equipmentCategories;

    private DamageDTO damage;

    @JsonProperty("two_handed_damage")
    private DamageDTO twoHandedDamage;
}

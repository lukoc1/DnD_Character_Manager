package pl.visa.dndCM.equipmentItem;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import pl.visa.dndCM.equipmentItem.damageType.DamageType;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentItemDTO {

    private String index;
    private String name;


    private EquipmentCategory category;

    @JsonIgnore
    private String damage;
    @JsonIgnore
    private String twoHandedDamage;

    private DamageType damageType;

    @JsonProperty("equipment_categories")
    private void unpackCategory(List<Map<String, Object>> categories) {
        if (categories != null && !categories.isEmpty()) {
            Map<String, Object> firstCategory = categories.get(0);

            // Najpierw sprawdzamy pole 'index' (np. "weapon", "mounts-and-vehicles"),
            // a jeśli go nie ma – sprawdzamy 'name'
            String catIdentifier = (String) firstCategory.getOrDefault("index", firstCategory.get("name"));
            this.category = mapCategoryIdentifier(catIdentifier);
        }
    }

    @JsonProperty("damage")
    private void unpackDamage(Map<String, Object> damageNode) {
        if (damageNode != null && damageNode.containsKey("damage_dice")) {
            this.damage = (String) damageNode.get("damage_dice");
        }
    }

    @JsonProperty("two_handed_damage")
    private void unpackTwoHandedDamage(Map<String, Object> twoHandedNode) {
        if (twoHandedNode != null && twoHandedNode.containsKey("damage_dice")) {
            this.twoHandedDamage = (String) twoHandedNode.get("damage_dice");
        }
    }

    private EquipmentCategory mapCategoryIdentifier(String catIndex) {
        if (catIndex == null) {
            return EquipmentCategory.ADVENTURING_GEAR;
        }

        return switch (catIndex.toLowerCase().trim()) {
            // WEAPONS
            case "weapons", "simple-weapons", "martial-weapons",
                 "simple-melee-weapons", "simple-ranged-weapons",
                 "martial-melee-weapons", "martial-ranged-weapons",
                 "melee-weapons", "ranged-weapons"
                    -> EquipmentCategory.WEAPONS;

            // ARMOR
            case "armor", "light-armor", "medium-armor", "heavy-armor", "shields"
                    -> EquipmentCategory.ARMOR;

            // AMMUNITION
            case "ammunition"
                    -> EquipmentCategory.AMMUNITION;

            // TOOLS
            case "tools", "artisans-tools", "gaming-sets", "musical-instruments", "other-tools"
                    -> EquipmentCategory.TOOLS;

            // MAGIC_ITEMS / SPECIAL FOCI
            case "potions", "rings", "staffs", "wands", "wondrous-items",
                 "arcane-foci", "druidic-foci", "holy-symbols"
                    -> EquipmentCategory.MAGIC_ITEMS;

            // ADVENTURING GEAR / PACKS
            case "adventuring-gear", "equipment-packs"
                    -> EquipmentCategory.ADVENTURING_GEAR;

            // POZOSTAŁE (MOUNT, VEHICLES, SERVICES, CRAFTING, COINS)
            case "mounts-and-vehicles" -> EquipmentCategory.MOUNTS_AND_VEHICLES;
            case "services" -> EquipmentCategory.SERVICES;
            case "crafting-equipment" -> EquipmentCategory.CRAFTING_EQUIPMENT;
            case "coins", "standard-gear" -> EquipmentCategory.COINS;

            default -> EquipmentCategory.ADVENTURING_GEAR;
        };
    }
}

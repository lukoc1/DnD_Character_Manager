package pl.visa.dndCM.gameData.equipmentItem;

import java.util.List;
import java.util.Set;

public enum EquipmentCategory {
    COINS,
    WEAPONS,
    AMMUNITION,
    ARMOR,
    TOOLS,
    ADVENTURING_GEAR,
    MOUNTS_AND_VEHICLES,
    SERVICES,
    MAGIC_ITEMS,
    CRAFTING_EQUIPMENT;

    private static final Set<String> MAGIC_ITEM_INDEXES = Set.of(
            "potions", "rings", "staffs", "wands", "wondrous-items",
            "arcane-foci", "druidic-foci", "holy-symbols");


    public static EquipmentCategory fromApiCategories(List<String> indexes) {
        if (indexes == null || indexes.isEmpty()) {
            return ADVENTURING_GEAR;
        }
        if (indexes.stream().anyMatch(i -> i.contains("weapon"))) {
            return WEAPONS;
        }
        if (indexes.stream().anyMatch(i -> i.contains("armor")) || indexes.contains("shields")) {
            return ARMOR;
        }
        if (indexes.contains("ammunition")) {
            return AMMUNITION;
        }
        if (indexes.stream().anyMatch(i -> i.contains("tools")
                || i.equals("gaming-sets") || i.equals("musical-instruments"))) {
            return TOOLS;
        }
        if (indexes.stream().anyMatch(MAGIC_ITEM_INDEXES::contains)) {
            return MAGIC_ITEMS;
        }
        return ADVENTURING_GEAR;
    }
}

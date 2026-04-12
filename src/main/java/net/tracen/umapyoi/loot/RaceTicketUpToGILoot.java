package net.tracen.umapyoi.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.tracen.umapyoi.Umapyoi;

import java.util.Set;

public class RaceTicketUpToGILoot implements AddLootTableModifier {
    static private final Set<ResourceKey<LootTable>> TARGET_LOOT_TABLES = Set.of(
            BuiltInLootTables.ABANDONED_MINESHAFT,
            BuiltInLootTables.ANCIENT_CITY,
            BuiltInLootTables.ANCIENT_CITY_ICE_BOX,
            BuiltInLootTables.BASTION_BRIDGE,
            BuiltInLootTables.BASTION_HOGLIN_STABLE,
            BuiltInLootTables.BASTION_OTHER,
            BuiltInLootTables.BASTION_TREASURE,
            BuiltInLootTables.DESERT_PYRAMID,
            BuiltInLootTables.IGLOO_CHEST,
            BuiltInLootTables.JUNGLE_TEMPLE,
            BuiltInLootTables.NETHER_BRIDGE,
            BuiltInLootTables.SHIPWRECK_MAP,
            BuiltInLootTables.SHIPWRECK_SUPPLY,
            BuiltInLootTables.SHIPWRECK_TREASURE,
            BuiltInLootTables.SIMPLE_DUNGEON,
            BuiltInLootTables.STRONGHOLD_CORRIDOR,
            BuiltInLootTables.STRONGHOLD_CROSSING,
            BuiltInLootTables.STRONGHOLD_LIBRARY,
            BuiltInLootTables.UNDERWATER_RUIN_BIG,
            BuiltInLootTables.UNDERWATER_RUIN_SMALL,
            BuiltInLootTables.WOODLAND_MANSION
    );

    @Override
    public Set<ResourceKey<LootTable>> targetLootTables() {
        return TARGET_LOOT_TABLES;
    }

    @Override
    public ResourceKey<LootTable> lootTable() {
        return ResourceKey.create(
                Registries.LOOT_TABLE,
                Identifier.fromNamespaceAndPath(Umapyoi.MODID, "race/ticket/race_ticket_up_to_gi")
        );
    }
}

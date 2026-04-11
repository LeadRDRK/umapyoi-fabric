package net.tracen.umapyoi.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.tracen.umapyoi.Umapyoi;

import java.util.Set;

public class RareSkillBooksLoot implements AddLootTableModifier {
    static private final Set<ResourceKey<LootTable>> TARGET_LOOT_TABLES = Set.of(
            BuiltInLootTables.ABANDONED_MINESHAFT,
            BuiltInLootTables.JUNGLE_TEMPLE,
            BuiltInLootTables.SIMPLE_DUNGEON,
            BuiltInLootTables.STRONGHOLD_LIBRARY,
            BuiltInLootTables.WOODLAND_MANSION,
            BuiltInLootTables.SHIPWRECK_TREASURE,
            BuiltInLootTables.BURIED_TREASURE,
            BuiltInLootTables.DESERT_PYRAMID,
            BuiltInLootTables.SPAWN_BONUS_CHEST,
            BuiltInLootTables.BASTION_BRIDGE,
            BuiltInLootTables.BASTION_HOGLIN_STABLE,
            BuiltInLootTables.BASTION_OTHER,
            BuiltInLootTables.BASTION_TREASURE,
            BuiltInLootTables.END_CITY_TREASURE,
            BuiltInLootTables.NETHER_BRIDGE,
            BuiltInLootTables.STRONGHOLD_CORRIDOR,
            BuiltInLootTables.UNDERWATER_RUIN_BIG,
            BuiltInLootTables.PILLAGER_OUTPOST
    );

    @Override
    public Set<ResourceKey<LootTable>> targetLootTables() {
        return TARGET_LOOT_TABLES;
    }

    @Override
    public ResourceKey<LootTable> lootTable() {
        return ResourceKey.create(
                Registries.LOOT_TABLE,
                Identifier.fromNamespaceAndPath(Umapyoi.MODID, "complex_skills")
        );
    }
}

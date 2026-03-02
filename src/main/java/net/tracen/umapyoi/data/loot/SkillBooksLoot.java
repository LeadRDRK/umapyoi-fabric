package net.tracen.umapyoi.data.loot;

import net.minecraft.resources.ResourceLocation;
import net.tracen.umapyoi.Umapyoi;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SkillBooksLoot implements AddLootTableModifier {
    static private final Set<ResourceLocation> TARGET_LOOT_TABLES = Stream.of(
                    "minecraft:chests/abandoned_mineshaft",
                    "minecraft:chests/jungle_temple",
                    "minecraft:chests/simple_dungeon",
                    "minecraft:chests/stronghold_library",
                    "minecraft:chests/woodland_mansion",
                    "minecraft:chests/shipwreck_treasure",
                    "minecraft:chests/buried_treasure",
                    "minecraft:chests/desert_pyramid",
                    "minecraft:chests/spawn_bonus_chest"
            )
            .map(ResourceLocation::new)
            .collect(Collectors.toUnmodifiableSet());

    @Override
    public Set<ResourceLocation> targetLootTables() {
        return TARGET_LOOT_TABLES;
    }

    @Override
    public ResourceLocation lootTable() {
        return new ResourceLocation(Umapyoi.MODID, "simple_skills");
    }
}

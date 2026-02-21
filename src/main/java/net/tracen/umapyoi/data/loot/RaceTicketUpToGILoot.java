package net.tracen.umapyoi.data.loot;

import net.minecraft.resources.ResourceLocation;
import net.tracen.umapyoi.Umapyoi;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RaceTicketUpToGILoot implements AddLootTableModifier {
    static private final Set<ResourceLocation> TARGET_LOOT_TABLES = Stream.of(
                    "minecraft:chests/abandoned_mineshaft",
                    "minecraft:chests/ancient_city",
                    "minecraft:chests/ancient_city_ice_box",
                    "minecraft:chests/bastion_bridge",
                    "minecraft:chests/bastion_hoglin_stable",
                    "minecraft:chests/bastion_other",
                    "minecraft:chests/bastion_treasure",
                    "minecraft:chests/desert_pyramid",
                    "minecraft:chests/igloo_chest",
                    "minecraft:chests/jungle_temple",
                    "minecraft:chests/nether_bridge",
                    "minecraft:chests/shipwreck_map",
                    "minecraft:chests/shipwreck_supply",
                    "minecraft:chests/shipwreck_treasure",
                    "minecraft:chests/simple_dungeon",
                    "minecraft:chests/stronghold_corridor",
                    "minecraft:chests/stronghold_crossing",
                    "minecraft:chests/stronghold_library",
                    "minecraft:chests/underwater_ruin_big",
                    "minecraft:chests/underwater_ruin_small",
                    "minecraft:chests/woodland_mansion"
            )
            .map(ResourceLocation::new)
            .collect(Collectors.toUnmodifiableSet());

    @Override
    public Set<ResourceLocation> targetLootTables() {
        return TARGET_LOOT_TABLES;
    }

    @Override
    public ResourceLocation lootTable() {
        return new ResourceLocation(Umapyoi.MODID, "race/ticket/race_ticket_up_to_gi");
    }
}

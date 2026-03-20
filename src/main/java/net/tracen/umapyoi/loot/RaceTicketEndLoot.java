package net.tracen.umapyoi.loot;

import net.minecraft.resources.ResourceLocation;
import net.tracen.umapyoi.Umapyoi;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RaceTicketEndLoot implements AddLootTableModifier {
    static private final Set<ResourceLocation> TARGET_LOOT_TABLES = Stream.of(
                    "minecraft:chests/end_city_treasure"
            )
            .map(ResourceLocation::new)
            .collect(Collectors.toUnmodifiableSet());

    @Override
    public Set<ResourceLocation> targetLootTables() {
        return TARGET_LOOT_TABLES;
    }

    @Override
    public ResourceLocation lootTable() {
        return new ResourceLocation(Umapyoi.MODID, "race/ticket/race_ticket_end");
    }
}

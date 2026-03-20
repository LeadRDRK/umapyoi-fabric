package net.tracen.umapyoi.loot;

import net.minecraft.resources.ResourceLocation;
import net.tracen.umapyoi.Umapyoi;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RaceTicketUpToOpLoot implements AddLootTableModifier {
    static private final Set<ResourceLocation> TARGET_LOOT_TABLES = Stream.of(
                    "minecraft:chests/village/village_butcher",
                    "minecraft:chests/village/village_cartographer",
                    "minecraft:chests/village/village_desert_house",
                    "minecraft:chests/village/village_fisher",
                    "minecraft:chests/village/village_fletcher",
                    "minecraft:chests/village/village_mason",
                    "minecraft:chests/village/village_plains_house",
                    "minecraft:chests/village/village_savanna_house",
                    "minecraft:chests/village/village_shepherd",
                    "minecraft:chests/village/village_snowy_house",
                    "minecraft:chests/village/village_taiga_house",
                    "minecraft:chests/village/village_tannery",
                    "minecraft:chests/village/village_temple",
                    "minecraft:chests/village/village_toolsmith",
                    "minecraft:chests/village/village_weaponsmith"
            )
            .map(ResourceLocation::new)
            .collect(Collectors.toUnmodifiableSet());

    @Override
    public Set<ResourceLocation> targetLootTables() {
        return TARGET_LOOT_TABLES;
    }

    @Override
    public ResourceLocation lootTable() {
        return new ResourceLocation(Umapyoi.MODID, "race/ticket/race_ticket_up_to_op");
    }
}

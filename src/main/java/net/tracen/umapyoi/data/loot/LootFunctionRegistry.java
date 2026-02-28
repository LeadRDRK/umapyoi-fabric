package net.tracen.umapyoi.data.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.registry.LazyRegistrar;
import net.tracen.umapyoi.registry.RegistryObject;

public class LootFunctionRegistry {
    public static final LazyRegistrar<LootItemFunctionType> LOOT_FUNCTIONS =
            LazyRegistrar.create(Registries.LOOT_FUNCTION_TYPE, Umapyoi.MODID);

    public static final RegistryObject<LootItemFunctionType> RACE_TICKET_RANDOM =
            LOOT_FUNCTIONS.register("race_ticket_random",
                    () -> new LootItemFunctionType(new RaceTicketRandomLootFunction.RaceTicketRandomLootSerializer()));
}

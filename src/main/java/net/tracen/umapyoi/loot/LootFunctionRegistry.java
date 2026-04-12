package net.tracen.umapyoi.loot;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.registry.LazyRegistrar;
import net.tracen.umapyoi.registry.RegistryObject;

public class LootFunctionRegistry {
    public static final LazyRegistrar<MapCodec<? extends LootItemFunction>> LOOT_FUNCTION_TYPES =
            LazyRegistrar.create(Registries.LOOT_FUNCTION_TYPE, Umapyoi.MODID);

    public static final RegistryObject<MapCodec<? extends LootItemFunction>> UMASKILL_WITH_LEVEL =
            LOOT_FUNCTION_TYPES.register("umaskill_with_level", () -> UmaSkillLootFunction.CODEC);

    public static final RegistryObject<MapCodec<? extends LootItemFunction>> RACE_TICKET_RANDOM =
            LOOT_FUNCTION_TYPES.register("race_ticket_random", () -> RaceTicketRandomLootFunction.CODEC);
}

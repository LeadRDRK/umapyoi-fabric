package net.tracen.umapyoi.villager;

import com.google.common.collect.ImmutableSet;

import net.fabricmc.fabric.api.object.builder.v1.world.poi.PoiHelper;
import net.fabricmc.fabric.api.registry.VillagerInteractionRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.TradeSet;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.data.trading.UmapyoiTradeSets;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public class VillageRegistry {
    public static final Identifier TRAINER_POI = Umapyoi.id("trainer_poi");

    public static final ResourceKey<VillagerProfession> TRAINER = createKey("trainer");

    private static ResourceKey<VillagerProfession> createKey(String name) {
        return ResourceKey.create(Registries.VILLAGER_PROFESSION, Umapyoi.id(name));
    }

    private static void register(
            ResourceKey<VillagerProfession> key,
            Identifier poi,
            SoundEvent sound,
            Int2ObjectMap<ResourceKey<TradeSet>> trades
    ) {
        Registry.register(
                BuiltInRegistries.VILLAGER_PROFESSION,
                key,
                new VillagerProfession(
                        Component.translatable("entity.minecraft.villager." + key.identifier().toLanguageKey()),
                        e -> e.is(poi),
                        e -> e.is(poi),
                        ImmutableSet.of(),
                        ImmutableSet.of(),
                        sound,
                        trades
                )
        );
    }

    public static void registerProfessions() {
        register(
                TRAINER,
                TRAINER_POI,
                SoundEvents.VILLAGER_WORK_LIBRARIAN,
                Int2ObjectMap.ofEntries(
                        Int2ObjectMap.entry(1, UmapyoiTradeSets.TRAINER_LEVEL_1),
                        Int2ObjectMap.entry(2, UmapyoiTradeSets.TRAINER_LEVEL_2),
                        Int2ObjectMap.entry(3, UmapyoiTradeSets.TRAINER_LEVEL_3),
                        Int2ObjectMap.entry(4, UmapyoiTradeSets.TRAINER_LEVEL_4),
                        Int2ObjectMap.entry(5, UmapyoiTradeSets.TRAINER_LEVEL_5)
                )
        );
    }

    public static void registerPoi() {
        PoiHelper.register(TRAINER_POI, 1, 1, BlockRegistry.TRAINING_FACILITY);
    }

    public static void registerHeroOfTheVillage() {
        VillagerInteractionRegistries.registerGiftLootTable(TRAINER,
                ResourceKey.create(Registries.LOOT_TABLE, Umapyoi.id("race/ticket/race_ticket_up_to_op_gameplay")));
    }

    public static void registerAll() {
        registerProfessions();
        registerPoi();
        registerHeroOfTheVillage();
    }
}

package net.tracen.umapyoi.villager;

import com.google.common.collect.ImmutableSet;

import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.fabric.api.registry.VillagerInteractionRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.block.BlockRegistry;

public class VillageRegistry {
    public static final Identifier TRAINER_POI = Umapyoi.id("trainer_poi");

    public static final ResourceKey<VillagerProfession> TRAINER = createKey("trainer");

    private static ResourceKey<VillagerProfession> createKey(String name) {
        return ResourceKey.create(Registries.VILLAGER_PROFESSION, Umapyoi.id(name));
    }

    private static void register(ResourceKey<VillagerProfession> key, Identifier poi, SoundEvent sound) {
        Registry.register(
                BuiltInRegistries.VILLAGER_PROFESSION,
                key,
                new VillagerProfession(
                        Component.translatable("entity.minecraft.villager." + key.location().toLanguageKey()),
                        e -> e.is(poi),
                        e -> e.is(poi),
                        ImmutableSet.of(),
                        ImmutableSet.of(),
                        sound
                )
        );
    }

    public static void registerProfessions() {
        register(TRAINER, TRAINER_POI, SoundEvents.VILLAGER_WORK_LIBRARIAN);
    }

    public static void registerPoi() {
        PointOfInterestHelper.register(TRAINER_POI, 1, 1, BlockRegistry.TRAINING_FACILITY);
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

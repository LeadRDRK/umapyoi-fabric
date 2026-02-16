package net.tracen.umapyoi.villager;

import com.google.common.collect.ImmutableSet;

import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.registry.LazyRegistrar;
import net.tracen.umapyoi.registry.RegistryObject;

public class VillageRegistry {
    public static final LazyRegistrar<VillagerProfession> PROFESSIONS = LazyRegistrar
            .create(Registries.VILLAGER_PROFESSION, Umapyoi.MODID);

    public static final Identifier TRAINER_POI = Identifier.fromNamespaceAndPath(Umapyoi.MODID, "trainer_poi");

    public static final RegistryObject<VillagerProfession> TRAINER = PROFESSIONS.register("trainer",
            () -> createProf("trainer", TRAINER_POI, SoundEvents.VILLAGER_WORK_LIBRARIAN));

    private static VillagerProfession createProf(String name, Identifier poi, SoundEvent sound) {
        return new VillagerProfession(Component.translatable("entity.minecraft.villager.umapyoi." + name),
                e -> e.is(poi), e -> e.is(poi),
                ImmutableSet.of(), ImmutableSet.of(), sound);
    }

    public static void registerPoi() {
        PointOfInterestHelper.register(TRAINER_POI, 1, 1, BlockRegistry.TRAINING_FACILITY);
    }
}

package net.tracen.umapyoi.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.tracen.umapyoi.Umapyoi;

public class SoundRegistry {
    public static final LazyRegistrar<SoundEvent> SOUNDS =
            LazyRegistrar.create(Registries.SOUND_EVENT, Umapyoi.MODID);

    public static final RegistryObject<SoundEvent> GATE_OPEN = SOUNDS.register("gate_open",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "gate_open")));

    public static final RegistryObject<SoundEvent> GATE_CLOSE = SOUNDS.register("gate_close",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "gate_close")));
}

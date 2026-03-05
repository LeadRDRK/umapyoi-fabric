package net.tracen.umapyoi.effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.registry.LazyRegistrar;
import net.tracen.umapyoi.registry.RegistryObject;

public class MobEffectRegistry {
    public static final LazyRegistrar<MobEffect> EFFECTS = LazyRegistrar.create(Registries.MOB_EFFECT,
            Umapyoi.MODID);

    public static final RegistryObject<MobEffect> PANICKING = EFFECTS.register("panicking", PanickingEffect::new);
    public static final RegistryObject<MobEffect> MOOD_BONUS = EFFECTS.register("mood_bonus", MoodBonus::new);
    public static final RegistryObject<MobEffect> SLOW_METABOLISM = EFFECTS.register("slow_metabolism", SlowMetabolismEffect::new);
    public static final RegistryObject<MobEffect> NIGHT_OWL = EFFECTS.register("night_owl", NightOwlEffect::new);
}

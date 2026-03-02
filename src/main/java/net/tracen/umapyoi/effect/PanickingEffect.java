package net.tracen.umapyoi.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.events.ResumeActionPointCallback;

public class PanickingEffect extends MobEffect {
    public PanickingEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    public static boolean onResumeAP(LivingEntity entity, ItemStack soul) {
        return entity.hasEffect(MobEffectRegistry.PANICKING.get());
    }

    public static void registerCallbacks() {
        ResumeActionPointCallback.EVENT.register(PanickingEffect::onResumeAP);
    }
}

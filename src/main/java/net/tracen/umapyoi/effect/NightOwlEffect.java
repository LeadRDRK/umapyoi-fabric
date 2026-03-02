package net.tracen.umapyoi.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.events.MotivationCallback;
import net.tracen.umapyoi.utils.UmaStatusUtils;

public class NightOwlEffect extends MobEffect {
    public NightOwlEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return (pDuration % 20) == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Level level = pLivingEntity.level();
        if (level.isClientSide) return;
        if (UmapyoiAPI.getUmaSoul(pLivingEntity).isEmpty()) return;
        if (level.random.nextDouble() <= Umapyoi.CONFIG.NIGHT_OWL_PROBABILITY_DOWN_MOTIVATION()) {
            UmaStatusUtils.changeMotivation(pLivingEntity, -1);
        }
    }

    public static boolean onMotivationChange(MotivationCallback.Context event) {
        LivingEntity target = event.getTarget();
        if (!target.hasEffect(MobEffectRegistry.NIGHT_OWL.get())) return false;
        return event.getDoTriggerBonus() || event.getAfter().compareTo(event.previous) < 0;
    }

    public static void registerCallbacks() {
        MotivationCallback.EVENT.register(NightOwlEffect::onMotivationChange);
    }
}

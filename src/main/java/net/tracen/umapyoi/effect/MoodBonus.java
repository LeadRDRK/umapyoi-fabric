package net.tracen.umapyoi.effect;

import static net.tracen.umapyoi.item.UmaSoulItem.propertyPercentageByValue;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.events.SettingPropertyCallback;
import net.tracen.umapyoi.events.UseSkillCallback;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import java.util.Optional;

public class MoodBonus extends MobEffect {
    public MoodBonus() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    public static void onSettingProperty(SettingPropertyCallback.Context event) {
        MobEffect thisEffect = MobEffectRegistry.MOOD_BONUS.get();
        LivingEntity entity = event.getLivingEntity();
        if (!entity.hasEffect(thisEffect)) return;
        MobEffectInstance instance = entity.getEffect(thisEffect);
        int levelAdd = Optional.ofNullable(instance).map(MobEffectInstance::getAmplifier).orElse(-1) + 1;
        ItemStack stack = event.getUmaSoul();
        int level = UmaSoulUtils.getProperty(stack)[event.getAspect().getId()] + levelAdd;
        double propertyFactor = propertyPercentageByValue(level);
        event.setPropertyPercentage(propertyFactor);
        event.setResultProperty(UmaSoulUtils.getMotivation(stack), event.getPropertyRate(), event.getRetiredValue(), propertyFactor);
    }

    public static boolean onBeforeUseSkill(UseSkillCallback.Context event) {
        MobEffect thisEffect = MobEffectRegistry.MOOD_BONUS.get();
        LivingEntity entity = event.getPlayer();
        if (!entity.hasEffect(thisEffect)) return false;
        event.setAp(event.getAp() / 2);
        return false;
    }

    public static void registerCallbacks() {
        SettingPropertyCallback.EVENT.register(MoodBonus::onSettingProperty);
        UseSkillCallback.EVENT.register(MoodBonus::onBeforeUseSkill);
    }
}

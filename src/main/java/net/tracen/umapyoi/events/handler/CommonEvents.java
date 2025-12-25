package net.tracen.umapyoi.events.handler;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.effect.MobEffectRegistry;
import net.tracen.umapyoi.events.ApplyFactorCallback;
import net.tracen.umapyoi.events.ApplyTrainingSupportCallback;
import net.tracen.umapyoi.events.LearnSkillCallback;
import net.tracen.umapyoi.registry.umadata.Motivations;
import net.tracen.umapyoi.utils.ResultRankingUtils;
import net.tracen.umapyoi.utils.UmaSkillUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils;

public class CommonEvents {
    public static boolean onDamageDownMotivation(LivingEntity entity, DamageSource source, float amount) {
        ItemStack soul = UmapyoiAPI.getUmaSoul(entity);
        if (soul.isEmpty())
            return true;
        if (amount < Umapyoi.CONFIG.DAMAGE_MOTIVATION_EFFECT())
            return true;
        if (Umapyoi.CONFIG.CHANCE_MOTIVATION_EFFECT() > 0) {
            if (entity.level().getRandom().nextDouble() <= Umapyoi.CONFIG.CHANCE_MOTIVATION_EFFECT())
                UmaStatusUtils.downMotivation(soul);
        }
        return true;
    }

    public static boolean onDamagePanicking(LivingEntity entity, DamageSource source, float amount) {
        ItemStack soul = UmapyoiAPI.getUmaSoul(entity);
        if (soul.isEmpty() || UmaSoulUtils.getMotivation(soul) != Motivations.BAD)
            return true;
        if (amount < Umapyoi.CONFIG.DAMAGE_MOTIVATION_EFFECT())
            return true;
        if (Umapyoi.CONFIG.CHANCE_MOTIVATION_EFFECT() > 0) {
            if (entity.level().getRandom().nextDouble() <= Umapyoi.CONFIG.CHANCE_MOTIVATION_EFFECT())
                entity.addEffect(new MobEffectInstance(MobEffectRegistry.PANICKING.get(), 3600));
        }
        return true;
    }

    public static boolean onTrainingFinished(ApplyTrainingSupportCallback.Context event) {
        var umaSoul = event.getUmaSoul();
        UmaSkillUtils.syncActionPoint(umaSoul);
        CompoundTag tag = umaSoul.getOrCreateTag();
        tag.putInt("resultRanking", ResultRankingUtils.generateRanking(umaSoul));
        return false;
    }

    public static void onFactorFinished(ApplyFactorCallback.Context event) {
        var umaSoul = event.getUmaSoul();
        UmaSkillUtils.syncActionPoint(umaSoul);
        CompoundTag tag = umaSoul.getOrCreateTag();
        tag.putInt("resultRanking", ResultRankingUtils.generateRanking(umaSoul));
    }

    public static void onSkillLearned(LearnSkillCallback.Context event) {
        var umaSoul = event.getUmaSoul();
        UmaSkillUtils.syncActionPoint(umaSoul);
        CompoundTag tag = umaSoul.getOrCreateTag();
        tag.putInt("resultRanking", ResultRankingUtils.generateRanking(umaSoul));
    }

    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(CommonEvents::onDamageDownMotivation);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(CommonEvents::onDamagePanicking);
        ApplyTrainingSupportCallback.Post.EVENT.register(CommonEvents::onTrainingFinished);
        ApplyFactorCallback.Post.EVENT.register(CommonEvents::onFactorFinished);
        LearnSkillCallback.EVENT.register(CommonEvents::onSkillLearned);
    }
}

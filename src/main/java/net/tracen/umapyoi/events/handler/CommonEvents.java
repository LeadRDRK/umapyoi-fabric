package net.tracen.umapyoi.events.handler;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.data.tag.UmapyoiItemTags;
import net.tracen.umapyoi.effect.MobEffectRegistry;
import net.tracen.umapyoi.events.ApplyFactorCallback;
import net.tracen.umapyoi.events.ApplyTrainingSupportCallback;
import net.tracen.umapyoi.events.LearnSkillCallback;
import net.tracen.umapyoi.events.LivingEntityUseItemEvents;
import net.tracen.umapyoi.events.PlayerSpawnPhantomsCallback;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.umadata.Motivations;
import net.tracen.umapyoi.registry.umadata.UmaDataExtraStatus;
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
            if (entity.level().getRandom().nextDouble() <= Umapyoi.CONFIG.CHANCE_MOTIVATION_EFFECT()) {
                if (entity.hasEffect(MobEffectRegistry.MOOD_BONUS.getHolder())) {
                    entity.removeEffect(MobEffectRegistry.MOOD_BONUS.getHolder());
                    return true;
                }
                entity.addEffect(new MobEffectInstance(MobEffectRegistry.PANICKING.getHolder(), 3600));
            }
        }
        return true;
    }

    public static boolean onTrainingFinished(ApplyTrainingSupportCallback.Context event) {
        var umaSoul = event.getUmaSoul();
        UmaSkillUtils.syncActionPoint(umaSoul);
        umaSoul.update(DataComponentsTypeRegistry.UMADATA_EXTRA_STATUS.get(), UmaDataExtraStatus.DEFAULT,
                data->new UmaDataExtraStatus(data.actionPoint(), data.extraActionPoint(),
                        ResultRankingUtils.generateRanking(umaSoul), data.motivation()));
        return false;
    }

    public static void onFactorFinished(ApplyFactorCallback.Context event) {
        var umaSoul = event.getUmaSoul();
        UmaSkillUtils.syncActionPoint(umaSoul);
        umaSoul.update(DataComponentsTypeRegistry.UMADATA_EXTRA_STATUS.get(), UmaDataExtraStatus.DEFAULT,
                data->new UmaDataExtraStatus(data.actionPoint(), data.extraActionPoint(),
                        ResultRankingUtils.generateRanking(umaSoul), data.motivation()));
    }

    public static void onSkillLearned(LearnSkillCallback.Context event) {
        var umaSoul = event.getUmaSoul();
        UmaSkillUtils.syncActionPoint(umaSoul);
        umaSoul.update(DataComponentsTypeRegistry.UMADATA_EXTRA_STATUS.get(), UmaDataExtraStatus.DEFAULT,
                data->new UmaDataExtraStatus(data.actionPoint(), data.extraActionPoint(),
                        ResultRankingUtils.generateRanking(umaSoul), data.motivation()));
    }

    public static void onConsumedItem(LivingEntity entity, ItemStack stack) {
        if (!stack.isEdible()) return;
        if (entity == null) return;
        if (entity.level().isClientSide()) return;
        Level world = entity.level();
        if (UmapyoiAPI.getUmaSoul(entity).isEmpty()) return;

        int statusCnt = Motivations.values().length;
        for (int i = 1 - statusCnt; i < statusCnt; i++) {
            if (i == 0) continue;
            if (stack.is(UmapyoiItemTags.getMotivationFoodTag(i))) {
                UmaStatusUtils.changeMotivation(entity, i);
                break;
            }
        }

        if (stack.is(UmapyoiItemTags.SLOW_METABOLISM)) {
            double p = Umapyoi.CONFIG.SLOW_METABOLISM_PROBABILITY();
            if (p != 0) {
                if (world.getRandom().nextDouble() <= p)
                    entity.addEffect(new MobEffectInstance(MobEffectRegistry.SLOW_METABOLISM.get(), 3600));
            }
        }
    }

    public static void onWorldTick(ServerLevel level) {
        for (ServerPlayer player: level.players()) {
            if (player.isSpectator()) continue;
            if (player.isSleeping()) {
                if (player.hasEffect(MobEffectRegistry.NIGHT_OWL.get()))
                    player.removeEffect(MobEffectRegistry.NIGHT_OWL.get());
            }
        }
    }

    public static void onPhantomEvent(ServerPlayer player) {
        if (player.isSpectator()) return;
        if (player.isSleeping()) return;
        if (UmapyoiAPI.getUmaSoul(player).isEmpty()) return;
        ServerStatsCounter serverstatscounter = player.getStats();
        int timeSinceRest = serverstatscounter.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        if (timeSinceRest >= Umapyoi.CONFIG.NIGHT_OWL_THRESHOLD()) {
            MobEffectInstance effectInstance = new MobEffectInstance(MobEffectRegistry.NIGHT_OWL.get(), -1);
            player.addEffect(effectInstance);
        }
    }

    public static void onPlayerSlept(LivingEntity entity, BlockPos sleepingPos) {
        if (entity instanceof ServerPlayer player) {
            if (player.hasEffect(MobEffectRegistry.NIGHT_OWL.get())) {
                player.removeEffect(MobEffectRegistry.NIGHT_OWL.get());
            }
        }
    }

    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(CommonEvents::onDamageDownMotivation);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(CommonEvents::onDamagePanicking);
        ApplyTrainingSupportCallback.Post.EVENT.register(CommonEvents::onTrainingFinished);
        ApplyFactorCallback.Post.EVENT.register(CommonEvents::onFactorFinished);
        LearnSkillCallback.EVENT.register(CommonEvents::onSkillLearned);
        LivingEntityUseItemEvents.FINISH.register(CommonEvents::onConsumedItem);
        ServerTickEvents.END_WORLD_TICK.register(CommonEvents::onWorldTick);
        PlayerSpawnPhantomsCallback.EVENT.register(CommonEvents::onPhantomEvent);
        EntitySleepEvents.START_SLEEPING.register(CommonEvents::onPlayerSlept);
    }
}

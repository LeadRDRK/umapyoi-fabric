package net.tracen.umapyoi.registry.skills;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.tracen.umapyoi.utils.UmaStatusUtils;

public class TopUmamusumeSkill extends UmaSkill {

    public TopUmamusumeSkill(Builder builder) {
        super(builder);
    }

    @Override
    public void applySkill(ServerLevel level, LivingEntity user) {
        UmaStatusUtils.addMotivation(user);
        user.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 300, 1));
        user.addEffect(new MobEffectInstance(MobEffects.SPEED, 100, 1));
    }

}

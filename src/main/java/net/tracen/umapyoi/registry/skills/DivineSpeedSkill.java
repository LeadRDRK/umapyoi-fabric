package net.tracen.umapyoi.registry.skills;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class DivineSpeedSkill extends UmaSkill {
    private final int life;

    public DivineSpeedSkill(Builder builder, int life) {
        super(builder);
        this.life = life;
    }

    @Override
    public void applySkill(ServerLevel level, LivingEntity user) {
        int skillLevel = this.getSkillLevel() - 1;
        user.addEffect(new MobEffectInstance(MobEffects.SPEED, this.getSpeedTime(), skillLevel));
        user.addEffect(new MobEffectInstance(MobEffects.SATURATION, this.getSkillLevel() * 10, skillLevel));
    }

    public int getSpeedTime() {
        return life;
    }

}

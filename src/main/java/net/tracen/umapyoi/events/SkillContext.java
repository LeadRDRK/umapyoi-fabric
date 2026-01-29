package net.tracen.umapyoi.events;

import net.minecraft.resources.ResourceLocation;
import net.tracen.umapyoi.registry.UmaSkillRegistry;
import net.tracen.umapyoi.registry.skills.UmaSkill;

public abstract class SkillContext {
    private final ResourceLocation skill;

    public SkillContext(ResourceLocation skill) {
        this.skill = skill;
    }

    public ResourceLocation getSkillResourceLocation() {
        return skill;
    }

    public UmaSkill getSkill() {
        return UmaSkillRegistry.REGISTRY.get().get(getSkillResourceLocation()).orElseThrow().value();
    }
}
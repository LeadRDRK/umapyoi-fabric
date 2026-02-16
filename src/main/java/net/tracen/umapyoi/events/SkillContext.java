package net.tracen.umapyoi.events;

import net.minecraft.resources.Identifier;
import net.tracen.umapyoi.registry.UmaSkillRegistry;
import net.tracen.umapyoi.registry.skills.UmaSkill;

public abstract class SkillContext {
    private final Identifier skill;

    public SkillContext(Identifier skill) {
        this.skill = skill;
    }

    public Identifier getSkillIdentifier() {
        return skill;
    }

    public UmaSkill getSkill() {
        return UmaSkillRegistry.REGISTRY.get().get(getSkillIdentifier()).orElseThrow().value();
    }
}
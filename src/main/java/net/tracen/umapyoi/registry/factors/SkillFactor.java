package net.tracen.umapyoi.registry.factors;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.registry.UmaSkillRegistry;
import net.tracen.umapyoi.registry.skills.UmaSkill;
import net.tracen.umapyoi.utils.UmaSkillUtils;

import java.util.Random;

public class SkillFactor extends UmaFactor {

    public SkillFactor() {
        super(FactorType.OTHER);
    }

    @Override
    public void applyFactor(ItemStack soul, UmaFactorStack stack) {
        Identifier skill = Identifier.tryParse(stack.getOrCreateTag().getString("skill").orElseThrow());
        if (skill != null && UmaSkillRegistry.REGISTRY.get().containsKey(skill)) {
            UmaSkill result = UmaSkillRegistry.REGISTRY.get().get(skill).orElseThrow().value();
            if(!result.isInheritable())
                return;

            Random rand = new Random();
            if (rand.nextFloat() < (stack.getLevel() * 0.25))
                UmaSkillUtils.learnSkill(soul, skill);
        }
    }

    @Override
    public Component getDescriptionDetail(UmaFactorStack stack) {
        Identifier skill = Identifier.tryParse(stack.getOrCreateTag().getString("skill").orElseThrow());
        if (skill != null && UmaSkillRegistry.REGISTRY.get().containsKey(skill)) {
            UmaSkill result = UmaSkillRegistry.REGISTRY.get().get(skill).orElseThrow().value();
            return result.getDescriptionDetail();
        }
        return Component.empty();
    }

    @Override
    public Component getDescription(UmaFactorStack stack) {
        Identifier skill = Identifier.tryParse(stack.getOrCreateTag().getString("skill").orElseThrow());
        if (skill != null && UmaSkillRegistry.REGISTRY.get().containsKey(skill)) {
            UmaSkill result = UmaSkillRegistry.REGISTRY.get().get(skill).orElseThrow().value();
            return result.getDescription().copy().append(" ")
                    .append(Component.translatable("enchantment.level." + stack.getLevel()));
        }
        return super.getDescription(stack);
    }

}

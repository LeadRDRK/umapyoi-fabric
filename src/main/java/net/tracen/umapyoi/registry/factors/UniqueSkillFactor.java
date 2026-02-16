package net.tracen.umapyoi.registry.factors;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.registry.UmaSkillRegistry;
import net.tracen.umapyoi.registry.skills.UmaSkill;
import net.tracen.umapyoi.utils.UmaSkillUtils;

public class UniqueSkillFactor extends UmaFactor {

    public UniqueSkillFactor() {
        super(FactorType.UNIQUE);
    }

    @Override
    public void applyFactor(ItemStack soul, UmaFactorStack stack) {
        Identifier skill = Identifier.tryParse(stack.getOrCreateTag().getString("skill").orElseThrow());
        UmaSkillUtils.learnSkill(soul, skill);
    }

    @Override
    public Component getDescription(UmaFactorStack stack) {
        Identifier skill = Identifier.tryParse(stack.getOrCreateTag().getString("skill").orElseThrow());
        if (skill != null && UmaSkillRegistry.REGISTRY.get().containsKey(skill)) {
            UmaSkill result = UmaSkillRegistry.REGISTRY.get().get(skill).orElseThrow().value();
            return result.getDescription();
        }
        return super.getDescription(stack);
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

}

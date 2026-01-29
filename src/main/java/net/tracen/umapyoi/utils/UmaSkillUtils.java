package net.tracen.umapyoi.utils;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.events.LearnSkillCallback;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.TrainingSupportRegistry;
import net.tracen.umapyoi.registry.UmaSkillRegistry;
import net.tracen.umapyoi.registry.skills.UmaSkill;
import net.tracen.umapyoi.registry.training.SupportStack;
import net.tracen.umapyoi.registry.training.card.SupportEntry;

public class UmaSkillUtils {

    public static SupportStack getSkillSupport(UmaSkill skill) {
        if (skill == null)
            return SupportStack.EMPTY;
        SupportStack result = new SupportStack(TrainingSupportRegistry.SKILL_SUPPORT.get(), 1);
        result.getOrCreateTag().putString("skill", skill.getRegistryName().toString());
        return result;
    }

    public static SupportEntry getSkillSupportEnrty(ResourceLocation skill) {
        if (skill == null)
            return null;
        SupportEntry result = new SupportEntry(TrainingSupportRegistry.SKILL_SUPPORT.getId(), 1);
        result.getOrCreateTag().putString("skill", skill.toString());
        return result;
    }
    
    public static ItemStack getSkillBook(UmaSkill skill) {
        if (skill == null)
            return ItemStack.EMPTY;
        ItemStack result = new ItemStack(ItemRegistry.SKILL_BOOK);
        result.update(DataComponentsTypeRegistry.DATA_LOCATION.get(), UmaSkillRegistry.BASIC_PACE.getId(),
                loc -> skill.getRegistryName());
        return result;
    }

    public static void syncActionPoint(ItemStack stack) {
        UmaSoulUtils.setActionPoint(stack, UmaSoulUtils.getMaxActionPoint(stack));
    }

    public static void learnSkill(ItemStack stack, ResourceLocation skill) {
        if (!UmaSoulUtils.hasEmptySkillSlot(stack))
            return;
        var skillItemOpt = UmaSkillRegistry.REGISTRY.get().get(skill);
        if (skillItemOpt.isPresent()) {
            UmaSkill skillItem = skillItemOpt.get().value();
            if(skillItem.getUpperSkill() !=null)
                if (hasLearnedSkill(stack, skillItem.getUpperSkill()))
                    return;

            int lowerSkillIndex = getLowerSkillIndex(stack, skill);
            if (lowerSkillIndex != -1)
                UmaSoulUtils.setSkill(stack, lowerSkillIndex, skill);

            if (!hasLearnedSkill(stack, skill))
                UmaSoulUtils.addSkill(stack, skill);

            var event = new LearnSkillCallback.Context(skill, stack);
            LearnSkillCallback.invoke(event);
        }
    }

    public static boolean hasLearnedSkill(ItemStack stack, ResourceLocation skill) {
        var skills = UmaSoulUtils.getSkills(stack);
        return skills.contains(skill);
    }
    
    public static int getLowerSkillIndex(ItemStack stack, ResourceLocation skill) {
        var skills = UmaSoulUtils.getSkills(stack);
        UmaSkill target = null;
        for(int i = 0;i<skills.size();i++) {
            target = UmaSkillRegistry.REGISTRY.get().get(skills.get(i)).map(Holder::value).orElse(null);
            if(target == null || target.getUpperSkill() == null)
                continue;
            if(target.getUpperSkill().equals(skill))
                return i;
        }
        // if doesn't have lower skill, return -1 for mark.
        return -1;
    }
}

package net.tracen.umapyoi.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.registry.UmaSkillRegistry;
import net.tracen.umapyoi.registry.skills.UmaSkill;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

public class SkillBookItem extends Item implements CreativeModeTabFiller {
    public SkillBookItem() {
        super(Umapyoi.defaultItemProperties().stacksTo(1));

    }

    @Environment(EnvType.CLIENT)
    @Override
    public void fillItemCategory(FabricItemGroupEntries entries) {
        for (UmaSkill skill : UmaSkillRegistry.REGISTRY.get()) {
            ItemStack result = getDefaultInstance();
            result.getOrCreateTag().putString("skill", skill.getRegistryName().toString());
            entries.accept(result);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(this.getSkill(stack).getDescription().copy().withStyle(ChatFormatting.GRAY));
        if(flagIn.isAdvanced() || Umapyoi.CONFIG.DISPLAY_DETAIL()) {
            tooltip.add(this.getSkill(stack).getDescriptionDetail().copy().withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public UmaSkill getSkill(ItemStack stack) {
        ResourceLocation skillID = Optional
                .ofNullable(ResourceLocation.tryParse(stack.getOrCreateTag().getString("skill")))
                .orElse(UmaSkillRegistry.BASIC_PACE.getId());
        if(!UmaSkillRegistry.REGISTRY.get().containsKey(skillID))
            skillID = UmaSkillRegistry.BASIC_PACE.getId();
        return UmaSkillRegistry.REGISTRY.get().get(skillID);
    }

}

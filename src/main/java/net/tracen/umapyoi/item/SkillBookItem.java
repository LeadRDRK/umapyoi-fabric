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
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.UmaSkillRegistry;
import net.tracen.umapyoi.registry.skills.UmaSkill;

import java.util.List;

public class SkillBookItem extends Item implements CreativeModeTabFiller {
    public SkillBookItem() {
        super(Umapyoi.defaultItemProperties().stacksTo(1));

    }

    @Environment(EnvType.CLIENT)
    @Override
    public void fillItemCategory(FabricItemGroupEntries entries) {
        for (ResourceLocation skill : UmaSkillRegistry.REGISTRY.get().keySet()) {
            ItemStack result = getDefaultInstance();
            result.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), skill);
            entries.accept(result);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(this.getSkill(stack).getDescription().copy().withStyle(ChatFormatting.GRAY));
        if(tooltipFlag.isAdvanced() || Umapyoi.CONFIG.DISPLAY_DETAIL) {
            tooltipComponents.add(this.getSkill(stack).getDescriptionDetail().copy().withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public UmaSkill getSkill(ItemStack stack) {
        ResourceLocation skillID = stack.getOrDefault(
                DataComponentsTypeRegistry.DATA_LOCATION.get(),
                UmaSkillRegistry.BASIC_PACE.getId()
        );
        return UmaSkillRegistry.REGISTRY.get().get(skillID);
    }

}

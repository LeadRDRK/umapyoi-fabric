package net.tracen.umapyoi.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.UmaSkillRegistry;
import net.tracen.umapyoi.registry.skills.UmaSkill;

import java.util.function.Consumer;

public class SkillBookItem extends Item implements CreativeModeTabFiller {
    public SkillBookItem(Properties p) {
        super(p);

    }

    @Environment(EnvType.CLIENT)
    @Override
    public void fillItemCategory(FabricItemGroupEntries entries) {
        for (Identifier skill : UmaSkillRegistry.REGISTRY.get().keySet()) {
            ItemStack result = getDefaultInstance();
            result.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), skill);
            entries.accept(result);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        tooltipAdder.accept(this.getSkill(stack).getDescription().copy().withStyle(ChatFormatting.GRAY));
        if(flag.isAdvanced() || Umapyoi.CONFIG.DISPLAY_DETAIL()) {
            tooltipAdder.accept(this.getSkill(stack).getDescriptionDetail().copy().withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public UmaSkill getSkill(ItemStack stack) {
        Identifier skillID = stack.getOrDefault(
                DataComponentsTypeRegistry.DATA_LOCATION.get(),
                UmaSkillRegistry.BASIC_PACE.getId()
        );
        return UmaSkillRegistry.REGISTRY.get().get(skillID)
                .map(Holder.Reference::value).orElse(null);
    }

}

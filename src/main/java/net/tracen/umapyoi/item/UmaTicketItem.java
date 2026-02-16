package net.tracen.umapyoi.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.tracen.umapyoi.data.builtin.UmaDataRegistry;
import net.tracen.umapyoi.data.tag.UmapyoiItemTags;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.training.card.SupportCard;
import net.tracen.umapyoi.utils.TrainingSupportUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import java.util.function.Consumer;

public class UmaTicketItem extends Item {
    public UmaTicketItem(Properties p) {
        super(p);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        if (stack.has(DataComponentsTypeRegistry.DATA_LOCATION.get())) {
            if(stack.is(UmapyoiItemTags.CARD_TICKET))
                tooltipAdder.accept(Component.translatable("tooltip.umapyoi.support_card.name",
                        TrainingSupportUtils.getTranslatedSupportCardName(this.getSupportCardID(stack))).withStyle(ChatFormatting.GRAY));
            else{tooltipAdder.accept(Component.translatable("tooltip.umapyoi.umadata.name",
                    UmaSoulUtils.getTranslatedUmaName(this.getUmaName(stack))).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    public Identifier getUmaName(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.DATA_LOCATION.get(), UmaDataRegistry.COMMON_UMA.identifier());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.has(DataComponentsTypeRegistry.DATA_LOCATION.get());
    }

    private Identifier getSupportCardID(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.DATA_LOCATION.get(), SupportCard.EMPTY_ID);
    }

}
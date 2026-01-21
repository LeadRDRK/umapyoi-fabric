package net.tracen.umapyoi.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.data.builtin.UmaDataRegistry;
import net.tracen.umapyoi.data.tag.UmapyoiItemTags;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.training.card.SupportCard;
import net.tracen.umapyoi.utils.TrainingSupportUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import java.util.List;

public class UmaTicketItem extends Item {
    public UmaTicketItem() {
        super(Umapyoi.defaultItemProperties());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (stack.has(DataComponentsTypeRegistry.DATA_LOCATION.get())) {
            if(stack.is(UmapyoiItemTags.CARD_TICKET))
                tooltipComponents.add(Component.translatable("tooltip.umapyoi.support_card.name",
                        TrainingSupportUtils.getTranslatedSupportCardName(this.getSupportCardID(stack))).withStyle(ChatFormatting.GRAY));
            else{tooltipComponents.add(Component.translatable("tooltip.umapyoi.umadata.name",
                    UmaSoulUtils.getTranslatedUmaName(this.getUmaName(stack))).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    public ResourceLocation getUmaName(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.DATA_LOCATION.get(), UmaDataRegistry.COMMON_UMA.location());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.has(DataComponentsTypeRegistry.DATA_LOCATION.get());
    }

    private ResourceLocation getSupportCardID(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.DATA_LOCATION.get(), SupportCard.EMPTY_ID);
    }

}
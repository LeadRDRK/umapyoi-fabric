package net.tracen.umapyoi.item.factor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.data.builtin.UmaDataRegistry;
import net.tracen.umapyoi.item.CreativeModeTabFiller;
import net.tracen.umapyoi.registry.UmaFactorRegistry;
import net.tracen.umapyoi.registry.factors.FactorType;
import net.tracen.umapyoi.registry.factors.UmaFactor;
import net.tracen.umapyoi.registry.factors.UmaFactorStack;
import net.tracen.umapyoi.utils.UmaFactorUtils;

import java.util.List;

public class UmaFactorContainerItem extends Item implements CreativeModeTabFiller {

    public UmaFactorContainerItem() {
        super(Umapyoi.defaultItemProperties().stacksTo(1));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void fillItemCategory(FabricItemGroupEntries entries) {
        for (UmaFactor factor : UmaFactorRegistry.REGISTRY.get()) {
            if (factor == UmaFactorRegistry.SKILL_FACTOR.get() || factor.getFactorType() == FactorType.UNIQUE)
                continue;
            List<UmaFactorStack> stackList = List.of(new UmaFactorStack(factor, 1));
            ItemStack result = getDefaultInstance();
            result.getOrCreateTag().putString("name", UmaDataRegistry.COMMON_UMA.location().toString());
            result.getOrCreateTag().put("factors", UmaFactorUtils.serializeNBT(stackList));
            entries.accept(result);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        CompoundTag tag = stack.getOrCreateTag();
        StringBuffer buffer = new StringBuffer("umadata.").append(tag.getString("name").toString().replace(':', '.'));
        tooltip.add(Component.translatable("tooltip.umapyoi.umadata.name", I18n.get(buffer.toString()))
                .withStyle(ChatFormatting.GRAY));
        if (Screen.hasShiftDown() || !Umapyoi.CONFIG.TOOLTIP_SWITCH()) {
            tooltip.add(Component.translatable("tooltip.umapyoi.factors.factors_details")
                    .withStyle(ChatFormatting.AQUA));
            List<UmaFactorStack> stackList = UmaFactorUtils.deserializeNBT(tag);

            stackList.stream().sorted(UmaFactorStack.UmaFactorStackComparator.INSTANCE).forEach(factor -> {
                switch (factor.getFactor().getFactorType()) {
                    case STATUS -> tooltip.add(factor.getDescription().copy().withStyle(ChatFormatting.BLUE));
                    case UNIQUE -> tooltip.add(factor.getDescription().copy().withStyle(ChatFormatting.GREEN));
                    case EXTRASTATUS -> tooltip.add(factor.getDescription().copy().withStyle(ChatFormatting.RED));
                    default -> tooltip.add(factor.getDescription().copy().withStyle(ChatFormatting.GRAY));
                }
                if(flagIn.isAdvanced() || Umapyoi.CONFIG.DISPLAY_DETAIL()) {
                    tooltip.add(factor.getDescriptionDetail().copy().withStyle(ChatFormatting.DARK_GRAY));
                }
            });
        } else {
            tooltip.add(Component.translatable("tooltip.umapyoi.press_shift_for_details")
                    .withStyle(ChatFormatting.AQUA));
        }
    }
}

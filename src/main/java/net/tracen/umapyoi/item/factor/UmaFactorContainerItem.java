package net.tracen.umapyoi.item.factor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.CreativeModeTabFiller;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.UmaFactorRegistry;
import net.tracen.umapyoi.registry.factors.FactorData;
import net.tracen.umapyoi.registry.factors.FactorType;
import net.tracen.umapyoi.registry.factors.UmaFactor;
import net.tracen.umapyoi.registry.factors.UmaFactorStack;
import net.tracen.umapyoi.registry.umadata.UmaData;
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
            result.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), UmaData.DEFAULT_UMA_ID);
            result.set(DataComponentsTypeRegistry.FACTOR_DATA.get(), UmaFactorUtils.serializeData(stackList));
            entries.accept(result);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        List<FactorData> datas = stack.get(DataComponentsTypeRegistry.FACTOR_DATA.get());
        String buffer = "umadata." + stack.get(DataComponentsTypeRegistry.DATA_LOCATION.get()).toLanguageKey();
        tooltipComponents.add(Component.translatable("tooltip.umapyoi.umadata.name", I18n.get(buffer.toString()))
                .withStyle(ChatFormatting.GRAY));
        if (Screen.hasShiftDown() || !Umapyoi.CONFIG.TOOLTIP_SWITCH()) {
            tooltipComponents.add(Component.translatable("tooltip.umapyoi.factors.factors_details")
                    .withStyle(ChatFormatting.AQUA));
            List<UmaFactorStack> stackList = UmaFactorUtils.deserializeData(datas);

            stackList.forEach(factor -> {
                switch (factor.getFactor().getFactorType()) {
                    case STATUS -> tooltipComponents.add(factor.getDescription().copy().withStyle(ChatFormatting.BLUE));
                    case UNIQUE -> tooltipComponents.add(factor.getDescription().copy().withStyle(ChatFormatting.GREEN));
                    case EXTRASTATUS -> tooltipComponents.add(factor.getDescription().copy().withStyle(ChatFormatting.RED));
                    default -> tooltipComponents.add(factor.getDescription().copy().withStyle(ChatFormatting.GRAY));
                }
                if(tooltipFlag.isAdvanced() || Umapyoi.CONFIG.DISPLAY_DETAIL()) {
                    tooltipComponents.add(factor.getDescriptionDetail().copy().withStyle(ChatFormatting.DARK_GRAY));
                }
            });
        } else {
            tooltipComponents.add(Component.translatable("tooltip.umapyoi.press_shift_for_details")
                    .withStyle(ChatFormatting.AQUA));
        }
    }
}

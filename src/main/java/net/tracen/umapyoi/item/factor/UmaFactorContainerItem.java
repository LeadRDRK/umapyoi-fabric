package net.tracen.umapyoi.item.factor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
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
import java.util.function.Consumer;

public class UmaFactorContainerItem extends Item implements CreativeModeTabFiller {

    public UmaFactorContainerItem(Properties p) {
        super(p);
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
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        List<FactorData> datas = stack.get(DataComponentsTypeRegistry.FACTOR_DATA.get());
        String buffer = "umadata." + stack.get(DataComponentsTypeRegistry.DATA_LOCATION.get()).toLanguageKey();
        tooltipAdder.accept(Component.translatable("tooltip.umapyoi.umadata.name", I18n.get(buffer.toString()))
                .withStyle(ChatFormatting.GRAY));
        if (Minecraft.getInstance().hasShiftDown() || !Umapyoi.CONFIG.TOOLTIP_SWITCH()) {
            tooltipAdder.accept(Component.translatable("tooltip.umapyoi.factors.factors_details")
                    .withStyle(ChatFormatting.AQUA));
            List<UmaFactorStack> stackList = UmaFactorUtils.deserializeData(datas);

            stackList.stream().sorted(UmaFactorStack.UmaFactorStackComparator.INSTANCE).forEach(factor -> {
                switch (factor.getFactor().getFactorType()) {
                    case STATUS -> tooltipAdder.accept(factor.getDescription().copy().withStyle(ChatFormatting.BLUE));
                    case UNIQUE -> tooltipAdder.accept(factor.getDescription().copy().withStyle(ChatFormatting.GREEN));
                    case EXTRASTATUS -> tooltipAdder.accept(factor.getDescription().copy().withStyle(ChatFormatting.RED));
                    default -> tooltipAdder.accept(factor.getDescription().copy().withStyle(ChatFormatting.GRAY));
                }
                if(flag.isAdvanced() || Umapyoi.CONFIG.DISPLAY_DETAIL()) {
                    tooltipAdder.accept(factor.getDescriptionDetail().copy().withStyle(ChatFormatting.DARK_GRAY));
                }
            });
        } else {
            tooltipAdder.accept(Component.translatable("tooltip.umapyoi.press_shift_for_details")
                    .withStyle(ChatFormatting.AQUA));
        }
    }
}

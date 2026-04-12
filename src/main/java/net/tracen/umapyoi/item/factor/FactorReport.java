package net.tracen.umapyoi.item.factor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.CreativeModeTabFiller;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.RegistryObject;
import net.tracen.umapyoi.registry.UmaFactorRegistry;
import net.tracen.umapyoi.registry.factors.FactorData;
import net.tracen.umapyoi.registry.factors.FactorType;
import net.tracen.umapyoi.registry.factors.UmaFactor;
import net.tracen.umapyoi.registry.factors.UmaFactorStack;
import net.tracen.umapyoi.utils.UmaFactorUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class FactorReport extends Item implements CreativeModeTabFiller {
    public FactorReport(Properties p) {
        super(p);
    }

    public static List<UmaFactorStack> getFactorStacks(ItemStack stack) {
        List<FactorData> datas = stack.get(DataComponentsTypeRegistry.FACTOR_DATA.get());
        return datas == null ? Collections.emptyList() : UmaFactorUtils.deserializeData(datas);
    }


    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                                TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder,
                                TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        getFactorStacks(stack).stream().forEach(factor -> {
            tooltipAdder.accept(factor.getDescription().copy().withStyle(switch (factor.getFactor().getFactorType()) {
                case STATUS -> ChatFormatting.BLUE;
                case UNIQUE -> ChatFormatting.GREEN;
                case EXTRASTATUS -> ChatFormatting.RED;
                default -> ChatFormatting.GRAY;
            }));
            if(flag.isAdvanced() || Umapyoi.CONFIG.DISPLAY_DETAIL()) {
                tooltipAdder.accept(factor.getDescriptionDetail().copy().withStyle(ChatFormatting.DARK_GRAY));
            }
        });
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void fillItemCategory(FabricCreativeModeTabOutput entries) {
        UmaFactorRegistry.FACTORS.getEntries().stream()
                .filter(i -> i.get().getFactorType() != FactorType.UNIQUE)
                .filter(i -> i != UmaFactorRegistry.SKILL_FACTOR)
                .sorted(UmaFactor.UmaFactorComparator.INSTANCE)
                .map(RegistryObject::get)
                .map(i -> new UmaFactorStack(i, i.getMaxLevel()))
                .map(i -> {
                    ItemStack result = ItemRegistry.FACTOR_SHARD.getDefaultInstance();
                    result.set(DataComponentsTypeRegistry.FACTOR_DATA.get(),
                            UmaFactorUtils.serializeData(List.of(i)));
                    return result;
                }).forEachOrdered(entries::accept);
    }
}

package net.tracen.umapyoi.item.factor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.CreativeModeTabFiller;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.RegistryObject;
import net.tracen.umapyoi.registry.UmaFactorRegistry;
import net.tracen.umapyoi.registry.factors.FactorType;
import net.tracen.umapyoi.registry.factors.UmaFactor;
import net.tracen.umapyoi.registry.factors.UmaFactorStack;
import net.tracen.umapyoi.utils.UmaFactorUtils;

import java.util.List;

import javax.annotation.Nullable;

public class FactorReport extends Item implements CreativeModeTabFiller {
    public FactorReport() {
        super(Umapyoi.defaultItemProperties());
    }

    public static List<UmaFactorStack> getFactorStacks(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return UmaFactorUtils.deserializeNBT(tag);
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        getFactorStacks(pStack).stream().forEach(factor -> {
            pTooltipComponents.add(factor.getDescription().copy().withStyle(switch (factor.getFactor().getFactorType()) {
                case STATUS -> ChatFormatting.BLUE;
                case UNIQUE -> ChatFormatting.GREEN;
                case EXTRASTATUS -> ChatFormatting.RED;
                default -> ChatFormatting.GRAY;
            }));
            if(Umapyoi.CONFIG.DISPLAY_DETAIL()) {
                pTooltipComponents.add(factor.getDescriptionDetail().copy().withStyle(ChatFormatting.DARK_GRAY));
            }
        });
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void fillItemCategory(FabricItemGroupEntries entries) {
        UmaFactorRegistry.FACTORS.getEntries().stream()
                .filter(i -> i.get().getFactorType() != FactorType.UNIQUE)
                .filter(i -> i != UmaFactorRegistry.SKILL_FACTOR)
                .sorted(UmaFactor.UmaFactorComparator.INSTANCE)
                .map(RegistryObject::get)
                .map(i -> new UmaFactorStack(i, i.getMaxLevel()))
                .map(i -> {
                    ItemStack result = ItemRegistry.FACTOR_SHARD.get().getDefaultInstance();
                    result.getOrCreateTag().put("factors", UmaFactorUtils.serializeNBT(List.of(i)));
                    return result;
                }).forEachOrdered(entries::accept);
    }
}

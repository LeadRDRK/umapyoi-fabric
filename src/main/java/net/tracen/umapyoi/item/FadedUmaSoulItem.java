package net.tracen.umapyoi.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.data.builtin.UmaDataRegistry;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.GachaRanking;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import java.util.List;
import java.util.Optional;

public class FadedUmaSoulItem extends Item implements CreativeModeTabFiller {

    public FadedUmaSoulItem() {
        super(Umapyoi.defaultItemProperties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents,
            TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.umapyoi.umadata.name",
                UmaSoulUtils.getTranslatedUmaName(this.getUmaName(pStack))).withStyle(ChatFormatting.GRAY));
    }

    public ResourceLocation getUmaName(ItemStack pStack) {
        if (pStack.getOrCreateTag().getString("name").isBlank())
            return UmaDataRegistry.COMMON_UMA.location();
        return Optional.ofNullable(ResourceLocation.tryParse(pStack.getOrCreateTag().getString("name")))
                .orElse(UmaDataRegistry.COMMON_UMA.location());
    }

    @Override
    public Component getName(ItemStack pStack) {
        GachaRanking ranking = GachaRanking.getGachaRanking(pStack);
        if(ranking == GachaRanking.EASTER_EGG) return super.getName(pStack).copy().withStyle(ChatFormatting.GREEN);
        return super.getName(pStack);
    }

    @Override
    public Rarity getRarity(ItemStack pStack) {
        GachaRanking ranking = GachaRanking.getGachaRanking(pStack);
        return ranking == GachaRanking.SSR || ranking == GachaRanking.EASTER_EGG ? Rarity.EPIC : ranking == GachaRanking.SR ? Rarity.UNCOMMON : Rarity.COMMON;
    }

    public static ItemStack genUmaSoul(String name, UmaData data) {
        ItemStack result = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
        result.getOrCreateTag().putString("name", name);
        result.getOrCreateTag().putString("identifier", data.getIdentifier().toString());
        result.getOrCreateTag().putString("ranking", data.getGachaRanking().toString().toLowerCase());
        return result;
    }

    @Override
    public void fillItemCategory(FabricItemGroupEntries entries) {
        UmaSoulItem.sortedUmaDataList(entries.getContext().holders()).forEach(
                entry -> {
//                    ItemStack result = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
//                    result.getOrCreateTag().putString("name", entry.key().location().toString());
//                    result.getOrCreateTag().putString("identifier", entry.value().getIdentifier().toString());
//                    result.getOrCreateTag().putString("ranking", entry.value().getGachaRanking().toString().toLowerCase());
                    ItemStack result = FadedUmaSoulItem.genUmaSoul(entry.key().location().toString(), entry.value());
                    entries.accept(result);
                }
        );
    }
}

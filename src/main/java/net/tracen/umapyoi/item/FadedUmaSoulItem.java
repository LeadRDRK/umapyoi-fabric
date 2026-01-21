package net.tracen.umapyoi.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.item.data.GachaRankingData;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.GachaRanking;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import java.util.List;

public class FadedUmaSoulItem extends Item implements CreativeModeTabFiller {

    public FadedUmaSoulItem() {
        super(Umapyoi.defaultItemProperties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.umapyoi.umadata.name",
                UmaSoulUtils.getTranslatedUmaName(this.getUmaName(stack))).withStyle(ChatFormatting.GRAY));
    }

    public ResourceLocation getUmaName(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.DATA_LOCATION.get(), UmaData.DEFAULT_UMA_ID);
    }

    @Override
    public Component getName(ItemStack pStack) {
        GachaRanking ranking = GachaRanking.getGachaRanking(pStack);
        if(ranking == GachaRanking.EASTER_EGG) return super.getName(pStack).copy().withStyle(ChatFormatting.GREEN);
        return super.getName(pStack);
    }

    public static ItemStack genUmaSoul(ResourceLocation name, UmaData data) {
        GachaRanking ranking = data.ranking();
        ItemStack result = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
        result.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), name);
        result.set(DataComponentsTypeRegistry.IDENTIFIER.get(), data.identifier());
        result.set(DataComponentsTypeRegistry.GACHA_RANKING.get(), new GachaRankingData(ranking));
        result.set(DataComponents.RARITY,
                ranking == GachaRanking.SSR || ranking == GachaRanking.EASTER_EGG ?
                        Rarity.EPIC :
                        ranking == GachaRanking.SR ?
                                Rarity.UNCOMMON :
                                Rarity.COMMON);
        return result;
    }

    @Override
    public void fillItemCategory(FabricItemGroupEntries entries) {
        UmaSoulItem.sortedUmaDataList(entries.getContext().holders()).forEach(
                entry -> {
                    ItemStack result = FadedUmaSoulItem.genUmaSoul(entry.key().location(), entry.value());
                    entries.accept(result);
                }
        );
    }
}

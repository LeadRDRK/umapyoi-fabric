package net.tracen.umapyoi.item;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.item.data.GachaRankingData;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.GachaRanking;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import java.util.Optional;
import java.util.function.Consumer;

public class FadedUmaSoulItem extends Item implements CreativeModeTabFiller {

    public FadedUmaSoulItem(Properties p) {
        super(p);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("tooltip.umapyoi.umadata.name",
                UmaSoulUtils.getTranslatedUmaName(this.getUmaName(stack))).withStyle(ChatFormatting.GRAY));
    }

    public Identifier getUmaName(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.DATA_LOCATION.get(), UmaData.DEFAULT_UMA_ID);
    }

    @Override
    public Component getName(ItemStack pStack) {
        GachaRanking ranking = GachaRanking.getGachaRanking(pStack);
        if(ranking == GachaRanking.EASTER_EGG) return super.getName(pStack).copy().withStyle(ChatFormatting.GREEN);
        return super.getName(pStack);
    }

    public static ItemStack genUmaSoul(Identifier name, UmaData data) {
        ItemStack result = ItemRegistry.BLANK_UMA_SOUL.getDefaultInstance();
        result.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), name);
        postProcessUmaSoul(result, data);
        return result;
    }

    public static void postProcessUmaSoul(ItemStack result, UmaData data) {
        GachaRanking ranking = data.ranking();
        result.set(DataComponentsTypeRegistry.IDENTIFIER.get(), data.identifier());
        result.set(DataComponentsTypeRegistry.GACHA_RANKING.get(), new GachaRankingData(ranking));
        result.set(DataComponents.RARITY,
                ranking == GachaRanking.SSR || ranking == GachaRanking.EASTER_EGG ?
                        Rarity.EPIC :
                        ranking == GachaRanking.SR ?
                                Rarity.UNCOMMON :
                                Rarity.COMMON);
    }

    @Override
    public void onCraftedPostProcess(ItemStack itemStack, Level level) {
        super.onCraftedPostProcess(itemStack, level);

        Optional.ofNullable(itemStack.get(DataComponentsTypeRegistry.DATA_LOCATION.get()))
                .flatMap(outputUma -> level.registryAccess()
                        .lookupOrThrow(UmaData.REGISTRY_KEY)
                        .get(ResourceKey.create(UmaData.REGISTRY_KEY, outputUma))
                )
                .ifPresent(data -> postProcessUmaSoul(itemStack, data.value()));
    }

    @Override
    public void fillItemCategory(FabricCreativeModeTabOutput entries) {
        UmaSoulItem.sortedUmaDataList(entries.getContext().holders()).forEach(
                entry -> {
                    ItemStack result = FadedUmaSoulItem.genUmaSoul(entry.key().identifier(), entry.value());
                    entries.accept(result);
                }
        );
    }
}

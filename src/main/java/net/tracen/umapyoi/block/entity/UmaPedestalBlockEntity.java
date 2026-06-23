package net.tracen.umapyoi.block.entity;

import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.config.UmapyoiConfig;
import net.tracen.umapyoi.data.builtin.UmaDataRegistry;
import net.tracen.umapyoi.data.tag.UmapyoiItemTags;
import net.tracen.umapyoi.item.FadedUmaSoulItem;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.GachaRanking;
import net.tracen.umapyoi.utils.GachaUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UmaPedestalBlockEntity extends AbstractPedestalBlockEntity implements Gachable {

    public UmaPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.UMA_PEDESTAL.get(), pos, state);
    }

    @Override
    protected ItemStack getResultItem() {
        if (this.level == null)
            return ItemStack.EMPTY;

        RandomSource rand = this.getLevel().getRandom();
        Registry<UmaData> registry = UmapyoiAPI.getUmaDataRegistry(this.getLevel());

        @NotNull
        Collection<ResourceLocation> keys = registry.keySet().stream()
                .filter(this.getFilter(getLevel(), getStoredItem()))
                .collect(Collectors.toCollection(Lists::newArrayList));

        ResourceLocation holder = keys.stream().skip(keys.isEmpty() ? 0 : rand.nextInt(keys.size())).findFirst()
                .orElse(UmaDataRegistry.COMMON_UMA.location());

//        ItemStack result = ItemRegistry.BLANK_UMA_SOUL.getDefaultInstance();
//        UmaData data = registry.get(holder);
//        result.getOrCreateTag().putString("name", holder.toString());
//        result.getOrCreateTag().putString("identifier", data.getIdentifier().toString());
//        result.getOrCreateTag().putString("ranking", data.getGachaRanking().toString().toLowerCase());
        ItemStack result = FadedUmaSoulItem.genUmaSoul(holder, registry.get(holder).orElseThrow().value());
        return result;
    }

    @Override
    protected boolean canWork() {
        ItemStack item = getStoredItem();
        return !item.isEmpty() && item.is(UmapyoiItemTags.UMA_TICKET);
    }

    @Override
    public Predicate<? super ResourceLocation> getFilter(Level level, ItemStack input) {
        return resloc -> {
            if (input.has(DataComponentsTypeRegistry.DATA_LOCATION.get())) {
                return resloc.equals(input.get(DataComponentsTypeRegistry.DATA_LOCATION.get()));
            }
            if (input.is(UmapyoiItemTags.SSR_UMA_TICKET))
                return UmapyoiAPI.getUmaDataRegistry(level).get(resloc).orElseThrow().value().ranking() == GachaRanking.SSR;
            if (input.is(UmapyoiItemTags.COMMON_GACHA_ITEM))
                return UmapyoiAPI.getUmaDataRegistry(level).get(resloc).orElseThrow().value().ranking() == GachaRanking.R;
            boolean cfgFlag = GachaUtils.checkGachaConfig();
            int gacha_roll;
            int ssrHit = cfgFlag ? Umapyoi.CONFIG.GACHA_PROBABILITY_SSR
                    : UmapyoiConfig.DEFAULT_GACHA_PROBABILITY_SSR;
            if (input.is(UmapyoiItemTags.SR_UMA_TICKET)) {
                gacha_roll = level.getRandom()
                        .nextInt(cfgFlag
                                ? Umapyoi.CONFIG.GACHA_PROBABILITY_SUM - Umapyoi.CONFIG.GACHA_PROBABILITY_R
                                : 30);

                return UmapyoiAPI.getUmaDataRegistry(level).get(resloc).orElseThrow().value()
                        .ranking() == (gacha_roll < ssrHit ? GachaRanking.SSR : GachaRanking.SR);
            }
            gacha_roll = level.getRandom().nextInt(
                    cfgFlag ? Umapyoi.CONFIG.GACHA_PROBABILITY_SUM : UmapyoiConfig.DEFAULT_GACHA_PROBABILITY_SUM);
            int srHit = ssrHit + (cfgFlag ? Umapyoi.CONFIG.GACHA_PROBABILITY_SR : UmapyoiConfig.DEFAULT_GACHA_PROBABILITY_SR);
            return UmapyoiAPI.getUmaDataRegistry(level).get(resloc).orElseThrow().value()
                    .ranking() == (gacha_roll < ssrHit ? GachaRanking.SSR
                            : gacha_roll < srHit ? GachaRanking.SR : GachaRanking.R);
        };
    }

}

package net.tracen.umapyoi.block.entity;

import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.UmapyoiConfigModel;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.data.tag.UmapyoiItemTags;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.training.card.SupportCard;
import net.tracen.umapyoi.utils.GachaRanking;
import net.tracen.umapyoi.utils.GachaUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SilverSupportAlbumPedestalBlockEntity extends AbstractSupportAlbumPedestalBlockEntity implements Gachable{

    public SilverSupportAlbumPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SILVER_SUPPORT_ALBUM_PEDESTAL.get(), pos, state);
    }

    @Override
    protected ItemStack getResultItem() {
        if (this.level == null)
            return ItemStack.EMPTY;

        RandomSource rand = this.getLevel().getRandom();
        Registry<SupportCard> registry = UmapyoiAPI.getSupportCardRegistry(this.getLevel());

        @NotNull
        Collection<Identifier> keys = registry.keySet().stream()
                .filter(this.getFilter(getLevel(), getStoredItem()))
                .collect(Collectors.toCollection(Lists::newArrayList));

        Identifier key = keys.stream().skip(keys.isEmpty() ? 0 : rand.nextInt(keys.size())).findFirst()
                .orElse(Identifier.fromNamespaceAndPath(Umapyoi.MODID, "blank_card"));

        ItemStack result = SupportCard.init(key, registry.get(key).orElseThrow().value());
        return result;
    }

    @Override
    protected boolean canWork() {
        return !getStoredItem().isEmpty() && getStoredItem().is(UmapyoiItemTags.CARD_TICKET) && !getStoredItem().is(UmapyoiItemTags.SSR_CARD_TICKET);
    }
    
    @Override
    public Predicate<? super Identifier> getFilter(Level level, ItemStack input) {
        return resloc -> {
            if (input.has(DataComponentsTypeRegistry.DATA_LOCATION.get())) {
                return resloc.equals(input.get(DataComponentsTypeRegistry.DATA_LOCATION.get()));
            }
            if (input.is(UmapyoiItemTags.SR_CARD_TICKET))
                return UmapyoiAPI.getSupportCardRegistry(level).get(resloc).orElseThrow().value().getGachaRanking() == GachaRanking.SR;
            if (input.is(UmapyoiItemTags.COMMON_GACHA_ITEM))
                return UmapyoiAPI.getSupportCardRegistry(level).get(resloc).orElseThrow().value().getGachaRanking() == GachaRanking.R;
            boolean cfgFlag = GachaUtils.checkGachaConfig();
            int gacha_roll;
            int ssrHit = cfgFlag ? Umapyoi.CONFIG.GACHA_PROBABILITY_SSR()
                    : UmapyoiConfigModel.DEFAULT_GACHA_PROBABILITY_SSR;
            gacha_roll = level.getRandom().nextInt(
                    cfgFlag ? Umapyoi.CONFIG.GACHA_PROBABILITY_SUM() : UmapyoiConfigModel.DEFAULT_GACHA_PROBABILITY_SUM);
            int srHit = ssrHit + (cfgFlag ? Umapyoi.CONFIG.GACHA_PROBABILITY_SR() : UmapyoiConfigModel.DEFAULT_GACHA_PROBABILITY_SR);
            return UmapyoiAPI.getSupportCardRegistry(level).get(resloc).orElseThrow().value()
                    .getGachaRanking() == (gacha_roll < srHit ? GachaRanking.SR : GachaRanking.R);
        };
    }

}

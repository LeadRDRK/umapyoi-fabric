package net.tracen.umapyoi.villager.itemlisting;

import net.minecraft.core.Registry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.UmaRaceTicketItem;
import net.tracen.umapyoi.registry.races.Race;
import net.tracen.umapyoi.registry.races.RaceRegistry;
import net.tracen.umapyoi.utils.RaceRanking;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public class RaceTicketItemListing implements VillagerTrades.ItemListing {
    private final int level;

    public RaceTicketItemListing(int level) {
        this.level = level;
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity pTrader, RandomSource pRandom) {
        Level world = pTrader.level();
        RaceRanking rk = RaceRanking.values()[this.level];
        Registry<Race> registry = UmapyoiAPI.getRaceRegistry(world);
        List<Race> fulfill = registry.stream()
                .filter(r -> r.ranking() == rk)
                .filter(r -> !Objects.equals(r.texturePredicateOverride(), RaceRegistry.PREDICATE_CHAMPIONS))
                .toList();
        Race rand = fulfill.get(pRandom.nextInt(fulfill.size()));
        ItemStack result = UmaRaceTicketItem.init(rand.id(), rand);
        int baseValPrice = 5 * this.level;
        int price = pRandom.nextIntBetweenInclusive(-2, 2) + baseValPrice;
        return new MerchantOffer(
                new ItemCost(ItemRegistry.JEWEL, price),
                result, 12, 6 * this.level, 0.2f
        );
    }
}

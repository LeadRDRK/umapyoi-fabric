package net.tracen.umapyoi.data.trading;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.data.tag.UmapyoiVillagerTradeTags;

import java.util.Optional;

public class UmapyoiTradeSets {
    public static final ResourceKey<TradeSet> TRAINER_LEVEL_1 = resourceKey("trainer/level_1");
    public static final ResourceKey<TradeSet> TRAINER_LEVEL_2 = resourceKey("trainer/level_2");
    public static final ResourceKey<TradeSet> TRAINER_LEVEL_3 = resourceKey("trainer/level_3");
    public static final ResourceKey<TradeSet> TRAINER_LEVEL_4 = resourceKey("trainer/level_4");
    public static final ResourceKey<TradeSet> TRAINER_LEVEL_5 = resourceKey("trainer/level_5");

    public static void bootstrap(final BootstrapContext<TradeSet> context) {
        register(context, TRAINER_LEVEL_1, UmapyoiVillagerTradeTags.TRAINER_LEVEL_1);
        register(context, TRAINER_LEVEL_2, UmapyoiVillagerTradeTags.TRAINER_LEVEL_2);
        register(context, TRAINER_LEVEL_3, UmapyoiVillagerTradeTags.TRAINER_LEVEL_3);
        register(context, TRAINER_LEVEL_4, UmapyoiVillagerTradeTags.TRAINER_LEVEL_4);
        register(context, TRAINER_LEVEL_5, UmapyoiVillagerTradeTags.TRAINER_LEVEL_5);
    }

    public static Holder.Reference<TradeSet> register(
            final BootstrapContext<TradeSet> context, final ResourceKey<TradeSet> resourceKey, final TagKey<VillagerTrade> tradeTag
    ) {
        return register(context, resourceKey, tradeTag, ConstantValue.exactly(2.0F));
    }

    public static Holder.Reference<TradeSet> register(
            final BootstrapContext<TradeSet> context, final ResourceKey<TradeSet> resourceKey, final TagKey<VillagerTrade> tradeTag, final NumberProvider numberProvider
    ) {
        return context.register(
                resourceKey,
                new TradeSet(
                        context.lookup(Registries.VILLAGER_TRADE).getOrThrow(tradeTag), numberProvider, false, Optional.of(resourceKey.identifier().withPrefix("trade_set/"))
                )
        );
    }

    public static ResourceKey<TradeSet> resourceKey(final String path) {
        return ResourceKey.create(Registries.TRADE_SET, Umapyoi.id(path));
    }
}

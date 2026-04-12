package net.tracen.umapyoi.data.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.tags.VillagerTradeTags;
import net.minecraft.world.item.trading.VillagerTrade;
import net.tracen.umapyoi.data.trading.UmapyoiVillagerTrades;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class UmapyoiVillagerTradeTagsProvider extends KeyTagProvider<VillagerTrade> {
    public UmapyoiVillagerTradeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.VILLAGER_TRADE, lookupProvider);
    }

    @Override
    protected void addTags(final HolderLookup.Provider registries) {
        tag(VillagerTradeTags.FARMER_LEVEL_2)
                .add(
                        UmapyoiVillagerTrades.FARMER_2_JEWEL_CUPCAKE
                );
        tag(VillagerTradeTags.FARMER_LEVEL_4)
                .add(
                        UmapyoiVillagerTrades.FARMER_4_JEWEL_SWEET_CUPCAKE
                );
        tag(VillagerTradeTags.CLERIC_LEVEL_3)
                .add(
                        UmapyoiVillagerTrades.CLERIC_3_EMERALD_JEWEL
                );
        tag(VillagerTradeTags.MASON_LEVEL_3)
                .add(
                        UmapyoiVillagerTrades.MASON_3_JEWEL_UMA_STATUE
                );
        tag(VillagerTradeTags.MASON_LEVEL_5)
                .add(
                        UmapyoiVillagerTrades.MASON_5_JEWEL_THREE_GODDESS
                );
        tag(VillagerTradeTags.SHEPHERD_LEVEL_5)
                .addAll(Stream.of(
                        UmapyoiVillagerTrades.SHEPHERD_5_JEWEL_SUMMER_UNIFORM,
                        UmapyoiVillagerTrades.SHEPHERD_5_JEWEL_WINTER_UNIFORM,
                        UmapyoiVillagerTrades.SHEPHERD_5_JEWEL_TRAINING_SUIT,
                        UmapyoiVillagerTrades.SHEPHERD_5_JEWEL_KINDERGARTEN_UNIFORM
                ));
        tag(UmapyoiVillagerTradeTags.TRAINER_LEVEL_1)
                .addAll(Stream.of(
                        UmapyoiVillagerTrades.TRAINER_1_CRYSTAL_SILVER_JEWEL,
                        UmapyoiVillagerTrades.TRAINER_1_HORSESHOE_SILVER_JEWEL,
                        UmapyoiVillagerTrades.TRAINER_1_JEWEL_UMA_TICKET,
                        UmapyoiVillagerTrades.TRAINER_1_JEWEL_CARD_TICKET,
                        UmapyoiVillagerTrades.TRAINER_1_JEWEL_RACE_TICKETS
                ));
        tag(UmapyoiVillagerTradeTags.TRAINER_LEVEL_2)
                .addAll(Stream.of(
                        UmapyoiVillagerTrades.TRAINER_2_JEWEL_TRAINING_ITEMS,
                        UmapyoiVillagerTrades.TRAINER_2_JEWEL_SKILL_BOOKS_1,
                        UmapyoiVillagerTrades.TRAINER_2_JEWEL_SKILL_BOOKS_2,
                        UmapyoiVillagerTrades.TRAINER_2_JEWEL_SKILL_BOOKS_3,
                        UmapyoiVillagerTrades.TRAINER_2_JEWEL_RACE_TICKETS
                ));
        tag(UmapyoiVillagerTradeTags.TRAINER_LEVEL_3)
                .addAll(Stream.of(
                        UmapyoiVillagerTrades.TRAINER_3_CRYSTAL_GOLD_JEWEL,
                        UmapyoiVillagerTrades.TRAINER_3_HORSESHOE_GOLD_JEWEL,
                        UmapyoiVillagerTrades.TRAINER_3_JEWEL_SR_UMA_TICKET,
                        UmapyoiVillagerTrades.TRAINER_3_JEWEL_SR_CARD_TICKET,
                        UmapyoiVillagerTrades.TRAINER_3_JEWEL_SKILL_BOOKS_1,
                        UmapyoiVillagerTrades.TRAINER_3_JEWEL_SKILL_BOOKS_2,
                        UmapyoiVillagerTrades.TRAINER_3_JEWEL_SKILL_BOOKS_3,
                        UmapyoiVillagerTrades.TRAINER_3_JEWEL_RACE_TICKETS
                ));
        tag(UmapyoiVillagerTradeTags.TRAINER_LEVEL_4)
                .addAll(Stream.of(
                        UmapyoiVillagerTrades.TRAINER_4_JEWEL_TRAINING_ITEMS,
                        UmapyoiVillagerTrades.TRAINER_4_JEWEL_SKILL_BOOKS_1,
                        UmapyoiVillagerTrades.TRAINER_4_JEWEL_SKILL_BOOKS_2,
                        UmapyoiVillagerTrades.TRAINER_4_JEWEL_SKILL_BOOKS_3,
                        UmapyoiVillagerTrades.TRAINER_4_JEWEL_RACE_TICKETS
                ));
        tag(UmapyoiVillagerTradeTags.TRAINER_LEVEL_5)
                .addAll(Stream.of(
                        UmapyoiVillagerTrades.TRAINER_5_CRYSTAL_RAINBOW_JEWEL,
                        UmapyoiVillagerTrades.TRAINER_5_HORSESHOE_RAINBOW_JEWEL,
                        UmapyoiVillagerTrades.TRAINER_5_JEWEL_SSR_UMA_TICKET,
                        UmapyoiVillagerTrades.TRAINER_5_JEWEL_SSR_CARD_TICKET,
                        UmapyoiVillagerTrades.TRAINER_5_JEWEL_SKILL_BOOKS_1,
                        UmapyoiVillagerTrades.TRAINER_5_JEWEL_SKILL_BOOKS_2,
                        UmapyoiVillagerTrades.TRAINER_5_JEWEL_SKILL_BOOKS_3,
                        UmapyoiVillagerTrades.TRAINER_5_JEWEL_TRAINING_ITEMS,
                        UmapyoiVillagerTrades.TRAINER_5_JEWEL_RACE_TICKETS
                ));
        tag(VillagerTradeTags.WANDERING_TRADER_COMMON)
                .addAll(Stream.of(
                        UmapyoiVillagerTrades.WANDERING_TRADER_JEWEL_HACHIMI_MID,
                        UmapyoiVillagerTrades.WANDERING_TRADER_JEWEL_HACHIMI_BIG,
                        UmapyoiVillagerTrades.WANDERING_TRADER_JEWEL_SMALL_ENERGY_DRINK,
                        UmapyoiVillagerTrades.WANDERING_TRADER_JEWEL_MEDIUM_ENERGY_DRINK
                ));
        tag(VillagerTradeTags.WANDERING_TRADER_UNCOMMON)
                .addAll(Stream.of(
                        UmapyoiVillagerTrades.WANDERING_TRADER_JEWEL_LARGE_ENERGY_DRINK,
                        UmapyoiVillagerTrades.WANDERING_TRADER_JEWEL_ROYAL_BITTER
                ));
    }
}

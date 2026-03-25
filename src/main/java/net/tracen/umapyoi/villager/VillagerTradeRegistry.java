package net.tracen.umapyoi.villager;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tracen.umapyoi.data.builtin.CostumeDataRegistry;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.UmaCostumeItem;
import net.tracen.umapyoi.villager.itemlisting.RaceTicketItemListing;
import net.tracen.umapyoi.villager.itemlisting.RandomItemOrderItemListing;
import net.tracen.umapyoi.villager.itemlisting.RandomPriceOrderItemListing;
import net.tracen.umapyoi.villager.itemlisting.RandomPriceSellItemListing;
import net.tracen.umapyoi.villager.itemlisting.SkillBooksItemListing;

public class VillagerTradeRegistry {
    public static void register() {
        registerOffers();
        registerTrainerOffers();
        registerWandererOffers();
    }

    private static void registerOffers() {
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 2, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.CUPCAKE), 1, 4, 16, 16, 5))
        );

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 4, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.SWEET_CUPCAKE), 2, 2, 16, 16, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 3, factories ->
            factories.add(new RandomPriceSellItemListing(new ItemStack(Items.EMERALD), 2, 1, 2, 8, 20))
        );

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.MASON, 3, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.UMA_STATUE), 1, 1, 1, 8, 20))
        );

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.MASON, 5, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.THREE_GODDESS), 1, 1, 1, 8, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.SHEPHERD, 5, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.SUMMER_UNIFORM), 1, 1, 1, 8, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.SHEPHERD, 5, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.WINTER_UNIFORM), 1, 1, 1, 8, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.SHEPHERD, 5, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.TRAINING_SUIT), 1, 1, 1, 8, 30, 0.2F))
        );

        var kindergartenUniform = UmaCostumeItem.getCostume(CostumeDataRegistry.KINDERGARTEN_UNIFORM.location());
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.SHEPHERD, 5, factories ->
            factories.add(new RandomPriceOrderItemListing(kindergartenUniform, 1, 1, 1, 8, 30, 0.2F))
        );
    }

    private static void registerTrainerOffers() {
        var trainer = ResourceKey.create(Registries.VILLAGER_PROFESSION, VillageRegistry.TRAINER.getId());

        TradeOfferHelper.registerVillagerOffers(trainer, 1, factories ->
            factories.add(new RandomPriceSellItemListing(new ItemStack(ItemRegistry.CRYSTAL_SILVER), 1, 1, 2, 16, 1))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 1, factories ->
            factories.add(new RandomPriceSellItemListing(new ItemStack(ItemRegistry.HORSESHOE_SILVER), 1, 1, 2, 16, 1))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 1, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.UMA_TICKET), 1, 1, 4, 16, 2))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 1, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.CARD_TICKET), 1, 1, 4, 16, 2))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 2, factories ->
            factories.add(new RandomItemOrderItemListing(Lists.newArrayList(new ItemStack(ItemRegistry.SPEED_LOW_ITEM),
                        new ItemStack(ItemRegistry.STAMINA_LOW_ITEM),
                        new ItemStack(ItemRegistry.STRENGTH_LOW_ITEM),
                        new ItemStack(ItemRegistry.MENTALITY_LOW_ITEM),
                        new ItemStack(ItemRegistry.WISDOM_LOW_ITEM)), 1, 2, 5, 16, 10))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 2, factories ->
            factories.add(new SkillBooksItemListing(10))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 3, factories ->
            factories.add(new RandomPriceSellItemListing(new ItemStack(ItemRegistry.CRYSTAL_GOLD), 3, 1, 2, 16, 10))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 3, factories ->
            factories.add(new RandomPriceSellItemListing(new ItemStack(ItemRegistry.HORSESHOE_GOLD), 3, 1, 2, 16, 10))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 3, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.SR_UMA_TICKET), 1, 1, 3, 12, 20))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 3, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.SR_CARD_TICKET), 1, 1, 3, 12, 20))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 3, factories ->
            factories.add(new SkillBooksItemListing(20))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 4, factories ->
            factories.add(new RandomItemOrderItemListing(Lists.newArrayList(new ItemStack(ItemRegistry.SPEED_MID_ITEM),
                        new ItemStack(ItemRegistry.STAMINA_MID_ITEM),
                        new ItemStack(ItemRegistry.STRENGTH_MID_ITEM),
                        new ItemStack(ItemRegistry.MENTALITY_MID_ITEM),
                        new ItemStack(ItemRegistry.WISDOM_MID_ITEM)), 2, 2, 5, 6, 30))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 4, factories ->
            factories.add(new SkillBooksItemListing(30))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 5, factories ->
            factories.add(new RandomPriceSellItemListing(new ItemStack(ItemRegistry.CRYSTAL_RAINBOW), 5, 1, 2, 16, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 5, factories ->
            factories.add(new RandomPriceSellItemListing(new ItemStack(ItemRegistry.HORSESHOE_RAINBOW), 5, 1, 2, 16, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 5, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.SSR_UMA_TICKET), 2, 1, 2, 12, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 5, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.SSR_CARD_TICKET), 2, 1, 2, 12, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 5, factories ->
            factories.add(new SkillBooksItemListing(30))
        );

        TradeOfferHelper.registerVillagerOffers(trainer, 5, factories ->
            factories.add(new RandomItemOrderItemListing(Lists.newArrayList(new ItemStack(ItemRegistry.SPEED_HIGH_ITEM),
                        new ItemStack(ItemRegistry.STAMINA_HIGH_ITEM),
                        new ItemStack(ItemRegistry.STRENGTH_HIGH_ITEM),
                        new ItemStack(ItemRegistry.MENTALITY_HIGH_ITEM),
                        new ItemStack(ItemRegistry.WISDOM_HIGH_ITEM)), 2, 2, 5, 6, 30, 0.2F))
        );

        for (int i = 1; i <= 5; i++) {
            var listing = new RaceTicketItemListing(i);
            TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), i, factories ->
                    factories.add(listing));
        }
    }

    private static void registerWandererOffers() {
        TradeOfferHelper.registerWanderingTraderOffers(builder -> {
            // Common trades
            builder.addOffersToPool(
                    TradeOfferHelper.WanderingTraderOffersBuilder.SELL_COMMON_ITEMS_POOL,
                    new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.HACHIMI_MID), 1, 1, 4, 32, 2)
            );
            builder.addOffersToPool(
                    TradeOfferHelper.WanderingTraderOffersBuilder.SELL_COMMON_ITEMS_POOL,
                    new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.HACHIMI_BIG), 2, 1, 4, 32, 2)
            );
            builder.addOffersToPool(
                    TradeOfferHelper.WanderingTraderOffersBuilder.SELL_COMMON_ITEMS_POOL,
                    new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.SMALL_ENERGY_DRINK), 1, 1, 4, 32, 2)
            );
            builder.addOffersToPool(
                    TradeOfferHelper.WanderingTraderOffersBuilder.SELL_COMMON_ITEMS_POOL,
                    new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.MEDIUM_ENERGY_DRINK), 2, 1, 4, 32, 2)
            );

            // "Rare" trades
            builder.addOffersToPool(
                    TradeOfferHelper.WanderingTraderOffersBuilder.SELL_SPECIAL_ITEMS_POOL,
                    new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.LARGE_ENERGY_DRINK), 3, 1, 2, 32, 15)
            );
            builder.addOffersToPool(
                    TradeOfferHelper.WanderingTraderOffersBuilder.SELL_SPECIAL_ITEMS_POOL,
                    new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.ROYAL_BITTER), 2, 1, 4, 32, 15)
            );
        });
    }
}

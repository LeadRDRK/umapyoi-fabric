package net.tracen.umapyoi.villager;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tracen.umapyoi.data.builtin.CostumeDataRegistry;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.UmaCostumeItem;
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
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.CUPCAKE.get()), 1, 4, 16, 16, 5))
        );

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 4, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.SWEET_CUPCAKE.get()), 2, 2, 16, 16, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 3, factories ->
            factories.add(new RandomPriceSellItemListing(new ItemStack(Items.EMERALD), 2, 1, 2, 8, 20))
        );

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.MASON, 3, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.UMA_STATUE.get()), 1, 1, 1, 8, 20))
        );

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.MASON, 5, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.THREE_GODDESS.get()), 1, 1, 1, 8, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.SHEPHERD, 5, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.SUMMER_UNIFORM.get()), 1, 1, 1, 8, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.SHEPHERD, 5, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.WINTER_UNIFORM.get()), 1, 1, 1, 8, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.SHEPHERD, 5, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.TRAINING_SUIT.get()), 1, 1, 1, 8, 30, 0.2F))
        );

        var kindergartenUniform = UmaCostumeItem.getCostume(CostumeDataRegistry.KINDERGARTEN_UNIFORM.location());
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.SHEPHERD, 5, factories ->
            factories.add(new RandomPriceOrderItemListing(kindergartenUniform, 1, 1, 1, 8, 30, 0.2F))
        );
    }

    private static void registerTrainerOffers() {
        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 1, factories ->
            factories.add(new RandomPriceSellItemListing(new ItemStack(ItemRegistry.CRYSTAL_SILVER.get()), 1, 1, 2, 16, 1))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 1, factories ->
            factories.add(new RandomPriceSellItemListing(new ItemStack(ItemRegistry.HORSESHOE_SILVER.get()), 1, 1, 2, 16, 1))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 1, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.UMA_TICKET.get()), 1, 1, 4, 16, 2))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 1, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.CARD_TICKET.get()), 1, 1, 4, 16, 2))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 2, factories ->
            factories.add(new RandomItemOrderItemListing(Lists.newArrayList(new ItemStack(ItemRegistry.SPEED_LOW_ITEM.get()),
                        new ItemStack(ItemRegistry.STAMINA_LOW_ITEM.get()),
                        new ItemStack(ItemRegistry.STRENGTH_LOW_ITEM.get()),
                        new ItemStack(ItemRegistry.MENTALITY_LOW_ITEM.get()),
                        new ItemStack(ItemRegistry.WISDOM_LOW_ITEM.get())), 1, 2, 5, 16, 10))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 2, factories ->
            factories.add(new SkillBooksItemListing(10))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 3, factories ->
            factories.add(new RandomPriceSellItemListing(new ItemStack(ItemRegistry.CRYSTAL_GOLD.get()), 3, 1, 2, 16, 10))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 3, factories ->
            factories.add(new RandomPriceSellItemListing(new ItemStack(ItemRegistry.HORSESHOE_GOLD.get()), 3, 1, 2, 16, 10))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 3, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.SR_UMA_TICKET.get()), 1, 1, 3, 12, 20))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 3, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.SR_CARD_TICKET.get()), 1, 1, 3, 12, 20))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 3, factories ->
            factories.add(new SkillBooksItemListing(20))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 4, factories ->
            factories.add(new RandomItemOrderItemListing(Lists.newArrayList(new ItemStack(ItemRegistry.SPEED_MID_ITEM.get()),
                        new ItemStack(ItemRegistry.STAMINA_MID_ITEM.get()),
                        new ItemStack(ItemRegistry.STRENGTH_MID_ITEM.get()),
                        new ItemStack(ItemRegistry.MENTALITY_MID_ITEM.get()),
                        new ItemStack(ItemRegistry.WISDOM_MID_ITEM.get())), 2, 2, 5, 6, 30))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 4, factories ->
            factories.add(new SkillBooksItemListing(30))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 5, factories ->
            factories.add(new RandomPriceSellItemListing(new ItemStack(ItemRegistry.CRYSTAL_RAINBOW.get()), 5, 1, 2, 16, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 5, factories ->
            factories.add(new RandomPriceSellItemListing(new ItemStack(ItemRegistry.HORSESHOE_RAINBOW.get()), 5, 1, 2, 16, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 5, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.SSR_UMA_TICKET.get()), 2, 1, 2, 12, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 5, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.SSR_CARD_TICKET.get()), 2, 1, 2, 12, 30, 0.2F))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 5, factories ->
            factories.add(new SkillBooksItemListing(30))
        );

        TradeOfferHelper.registerVillagerOffers(VillageRegistry.TRAINER.get(), 5, factories ->
            factories.add(new RandomItemOrderItemListing(Lists.newArrayList(new ItemStack(ItemRegistry.SPEED_HIGH_ITEM.get()),
                        new ItemStack(ItemRegistry.STAMINA_HIGH_ITEM.get()),
                        new ItemStack(ItemRegistry.STRENGTH_HIGH_ITEM.get()),
                        new ItemStack(ItemRegistry.MENTALITY_HIGH_ITEM.get()),
                        new ItemStack(ItemRegistry.WISDOM_HIGH_ITEM.get())), 2, 2, 5, 6, 30, 0.2F))
        );
    }

    private static void registerWandererOffers() {
        // Common trades
        TradeOfferHelper.registerWanderingTraderOffers(1, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.HACHIMI_MID.get()), 1, 1, 4, 32, 2))
        );
        TradeOfferHelper.registerWanderingTraderOffers(1, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.HACHIMI_BIG.get()), 2, 1, 4, 32, 2))
        );

        TradeOfferHelper.registerWanderingTraderOffers(1, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.SMALL_ENERGY_DRINK.get()), 1, 1, 4, 32, 2))
        );
        TradeOfferHelper.registerWanderingTraderOffers(2, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.MEDIUM_ENERGY_DRINK.get()), 2, 1, 4, 32, 2))
        );

        // "Rare" trades
        TradeOfferHelper.registerWanderingTraderOffers(4, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.LARGE_ENERGY_DRINK.get()), 3, 1, 2, 32, 15))
        );
        TradeOfferHelper.registerWanderingTraderOffers(5, factories ->
            factories.add(new RandomPriceOrderItemListing(new ItemStack(ItemRegistry.ROYAL_BITTER.get()), 2, 1, 4, 32, 15))
        );
    }
}

package net.tracen.umapyoi.data.trading;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.item.trading.VillagerTrades;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.Sum;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.data.builtin.CostumeDataRegistry;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.loot.RaceTicketRandomLootFunction;
import net.tracen.umapyoi.loot.UmaSkillLootFunction;
import net.tracen.umapyoi.registry.races.RaceRegistry;
import net.tracen.umapyoi.utils.RaceRanking;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class UmapyoiVillagerTrades {
    public static final ResourceKey<VillagerTrade> FARMER_2_JEWEL_CUPCAKE = resourceKey("farmer/2/jewel_cupcake");
    public static final ResourceKey<VillagerTrade> FARMER_4_JEWEL_SWEET_CUPCAKE = resourceKey("farmer/4/jewel_sweet_cupcake");
    public static final ResourceKey<VillagerTrade> CLERIC_3_EMERALD_JEWEL = resourceKey("cleric/3/emerald_jewel");
    public static final ResourceKey<VillagerTrade> MASON_3_JEWEL_UMA_STATUE = resourceKey("mason/3/jewel_uma_statue");
    public static final ResourceKey<VillagerTrade> MASON_5_JEWEL_THREE_GODDESS = resourceKey("mason/5/jewel_three_goddess");
    public static final ResourceKey<VillagerTrade> SHEPHERD_5_JEWEL_SUMMER_UNIFORM = resourceKey("shepherd/5/jewel_summer_uniform");
    public static final ResourceKey<VillagerTrade> SHEPHERD_5_JEWEL_WINTER_UNIFORM = resourceKey("shepherd/5/jewel_winter_uniform");
    public static final ResourceKey<VillagerTrade> SHEPHERD_5_JEWEL_TRAINING_SUIT = resourceKey("shepherd/5/jewel_training_suit");
    public static final ResourceKey<VillagerTrade> SHEPHERD_5_JEWEL_KINDERGARTEN_UNIFORM = resourceKey("shepherd/5/jewel_kindergarten_uniform");
    public static final ResourceKey<VillagerTrade> TRAINER_1_CRYSTAL_SILVER_JEWEL = resourceKey("trainer/1/crystal_silver_jewel");
    public static final ResourceKey<VillagerTrade> TRAINER_1_HORSESHOE_SILVER_JEWEL = resourceKey("trainer/1/horseshoe_silver_jewel");
    public static final ResourceKey<VillagerTrade> TRAINER_1_JEWEL_UMA_TICKET = resourceKey("trainer/1/jewel_uma_ticket");
    public static final ResourceKey<VillagerTrade> TRAINER_1_JEWEL_CARD_TICKET = resourceKey("trainer/1/jewel_card_ticket");
    public static final ResourceKey<VillagerTrade> TRAINER_2_JEWEL_TRAINING_ITEMS = resourceKey("trainer/2/jewel_training_items");
    public static final ResourceKey<VillagerTrade> TRAINER_2_JEWEL_SKILL_BOOKS_1 = resourceKey("trainer/2/jewel_skill_books_1");
    public static final ResourceKey<VillagerTrade> TRAINER_2_JEWEL_SKILL_BOOKS_2 = resourceKey("trainer/2/jewel_skill_books_2");
    public static final ResourceKey<VillagerTrade> TRAINER_2_JEWEL_SKILL_BOOKS_3 = resourceKey("trainer/2/jewel_skill_books_3");
    public static final ResourceKey<VillagerTrade> TRAINER_3_CRYSTAL_GOLD_JEWEL = resourceKey("trainer/3/crystal_gold_jewel");
    public static final ResourceKey<VillagerTrade> TRAINER_3_HORSESHOE_GOLD_JEWEL = resourceKey("trainer/3/horseshoe_gold_jewel");
    public static final ResourceKey<VillagerTrade> TRAINER_3_JEWEL_SR_UMA_TICKET = resourceKey("trainer/3/jewel_sr_uma_ticket");
    public static final ResourceKey<VillagerTrade> TRAINER_3_JEWEL_SR_CARD_TICKET = resourceKey("trainer/3/jewel_sr_card_ticket");
    public static final ResourceKey<VillagerTrade> TRAINER_3_JEWEL_SKILL_BOOKS_1 = resourceKey("trainer/3/jewel_skill_books_1");
    public static final ResourceKey<VillagerTrade> TRAINER_3_JEWEL_SKILL_BOOKS_2 = resourceKey("trainer/3/jewel_skill_books_2");
    public static final ResourceKey<VillagerTrade> TRAINER_3_JEWEL_SKILL_BOOKS_3 = resourceKey("trainer/3/jewel_skill_books_3");
    public static final ResourceKey<VillagerTrade> TRAINER_4_JEWEL_TRAINING_ITEMS = resourceKey("trainer/4/jewel_training_items");
    public static final ResourceKey<VillagerTrade> TRAINER_4_JEWEL_SKILL_BOOKS_1 = resourceKey("trainer/4/jewel_skill_books_1");
    public static final ResourceKey<VillagerTrade> TRAINER_4_JEWEL_SKILL_BOOKS_2 = resourceKey("trainer/4/jewel_skill_books_2");
    public static final ResourceKey<VillagerTrade> TRAINER_4_JEWEL_SKILL_BOOKS_3 = resourceKey("trainer/4/jewel_skill_books_3");
    public static final ResourceKey<VillagerTrade> TRAINER_5_CRYSTAL_RAINBOW_JEWEL = resourceKey("trainer/5/crystal_rainbow_jewel");
    public static final ResourceKey<VillagerTrade> TRAINER_5_HORSESHOE_RAINBOW_JEWEL = resourceKey("trainer/5/horseshoe_rainbow_jewel");
    public static final ResourceKey<VillagerTrade> TRAINER_5_JEWEL_SSR_UMA_TICKET = resourceKey("trainer/5/jewel_ssr_uma_ticket");
    public static final ResourceKey<VillagerTrade> TRAINER_5_JEWEL_SSR_CARD_TICKET = resourceKey("trainer/5/jewel_ssr_card_ticket");
    public static final ResourceKey<VillagerTrade> TRAINER_5_JEWEL_SKILL_BOOKS_1 = resourceKey("trainer/5/jewel_skill_books_1");
    public static final ResourceKey<VillagerTrade> TRAINER_5_JEWEL_SKILL_BOOKS_2 = resourceKey("trainer/5/jewel_skill_books_2");
    public static final ResourceKey<VillagerTrade> TRAINER_5_JEWEL_SKILL_BOOKS_3 = resourceKey("trainer/5/jewel_skill_books_3");
    public static final ResourceKey<VillagerTrade> TRAINER_5_JEWEL_TRAINING_ITEMS = resourceKey("trainer/5/jewel_training_items");
    public static final ResourceKey<VillagerTrade> TRAINER_1_JEWEL_RACE_TICKETS = resourceKey("trainer/1/jewel_race_tickets");
    public static final ResourceKey<VillagerTrade> TRAINER_2_JEWEL_RACE_TICKETS = resourceKey("trainer/2/jewel_race_tickets");
    public static final ResourceKey<VillagerTrade> TRAINER_3_JEWEL_RACE_TICKETS = resourceKey("trainer/3/jewel_race_tickets");
    public static final ResourceKey<VillagerTrade> TRAINER_4_JEWEL_RACE_TICKETS = resourceKey("trainer/4/jewel_race_tickets");
    public static final ResourceKey<VillagerTrade> TRAINER_5_JEWEL_RACE_TICKETS = resourceKey("trainer/5/jewel_race_tickets");
    public static final ResourceKey<VillagerTrade> WANDERING_TRADER_JEWEL_HACHIMI_MID = resourceKey("wandering_trader/jewel_hachimi_mid");
    public static final ResourceKey<VillagerTrade> WANDERING_TRADER_JEWEL_HACHIMI_BIG = resourceKey("wandering_trader/jewel_hachimi_big");
    public static final ResourceKey<VillagerTrade> WANDERING_TRADER_JEWEL_SMALL_ENERGY_DRINK = resourceKey("wandering_trader/jewel_small_energy_drink");
    public static final ResourceKey<VillagerTrade> WANDERING_TRADER_JEWEL_MEDIUM_ENERGY_DRINK = resourceKey("wandering_trader/jewel_medium_energy_drink");
    public static final ResourceKey<VillagerTrade> WANDERING_TRADER_JEWEL_LARGE_ENERGY_DRINK = resourceKey("wandering_trader/jewel_large_energy_drink");
    public static final ResourceKey<VillagerTrade> WANDERING_TRADER_JEWEL_ROYAL_BITTER = resourceKey("wandering_trader/jewel_royal_bitter");

    public static void bootstrap(final BootstrapContext<VillagerTrade> context) {
        registerOffers(context);
        registerTrainerOffers(context);
        registerWandererOffers(context);
    }

    public static ResourceKey<VillagerTrade> resourceKey(final String path) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, Umapyoi.id(path));
    }

    private static void registerOffers(final BootstrapContext<VillagerTrade> context) {
        registerRandomPriceOrderTrade(context, FARMER_2_JEWEL_CUPCAKE,
                ItemRegistry.CUPCAKE, 1, 4, 16, 16, 5);

        registerRandomPriceOrderTrade(context, FARMER_4_JEWEL_SWEET_CUPCAKE,
                ItemRegistry.SWEET_CUPCAKE, 2, 2, 16, 16, 30, 0.2F);

        registerRandomPriceSellTrade(context, CLERIC_3_EMERALD_JEWEL,
                Items.EMERALD, 2, 1, 2, 8, 20);

        registerRandomPriceOrderTrade(context, MASON_3_JEWEL_UMA_STATUE,
                ItemRegistry.UMA_STATUE, 1, 1, 1, 8, 20);

        registerRandomPriceOrderTrade(context, MASON_5_JEWEL_THREE_GODDESS,
                ItemRegistry.THREE_GODDESS, 1, 1, 1, 8, 30, 0.2F);

        registerRandomPriceOrderTrade(context, SHEPHERD_5_JEWEL_SUMMER_UNIFORM,
                ItemRegistry.SUMMER_UNIFORM, 1, 1, 1, 8, 30, 0.2F);

        registerRandomPriceOrderTrade(context, SHEPHERD_5_JEWEL_WINTER_UNIFORM,
                ItemRegistry.WINTER_UNIFORM, 1, 1, 1, 8, 30, 0.2F);

        registerRandomPriceOrderTrade(context, SHEPHERD_5_JEWEL_TRAINING_SUIT,
                ItemRegistry.TRAINING_SUIT, 1, 1, 1, 8, 30, 0.2F);

        var kindergartenUniform = new ItemStackTemplate(ItemRegistry.UMA_COSTUME, DataComponentPatch.builder()
                .set(DataComponentsTypeRegistry.DATA_LOCATION.get(), CostumeDataRegistry.KINDERGARTEN_UNIFORM.identifier())
                .build());
        registerRandomPriceOrderTrade(context, SHEPHERD_5_JEWEL_KINDERGARTEN_UNIFORM,
                kindergartenUniform, 1, 1, 1, 8, 30, 0.2F);
    }

    private static void registerTrainerOffers(final BootstrapContext<VillagerTrade> context) {
        registerRandomPriceSellTrade(context, TRAINER_1_CRYSTAL_SILVER_JEWEL,
                ItemRegistry.CRYSTAL_SILVER, 1, 1, 2, 16, 1);

        registerRandomPriceSellTrade(context, TRAINER_1_HORSESHOE_SILVER_JEWEL,
                ItemRegistry.HORSESHOE_SILVER, 1, 1, 2, 16, 1);

        registerRandomPriceOrderTrade(context, TRAINER_1_JEWEL_UMA_TICKET,
                ItemRegistry.UMA_TICKET, 1, 1, 4, 16, 2);

        registerRandomPriceOrderTrade(context, TRAINER_1_JEWEL_CARD_TICKET,
                ItemRegistry.CARD_TICKET, 1, 1, 4, 16, 2);

        registerRandomItemOrderTrade(context, TRAINER_2_JEWEL_TRAINING_ITEMS,
                List.of(ItemRegistry.SPEED_LOW_ITEM,
                        ItemRegistry.STAMINA_LOW_ITEM,
                        ItemRegistry.STRENGTH_LOW_ITEM,
                        ItemRegistry.MENTALITY_LOW_ITEM,
                        ItemRegistry.WISDOM_LOW_ITEM),
                1, 2, 5, 16, 10);

        registerSkillBooksTrade(context, TRAINER_2_JEWEL_SKILL_BOOKS_1, 1, 10);
        registerSkillBooksTrade(context, TRAINER_2_JEWEL_SKILL_BOOKS_2, 2, 10);
        registerSkillBooksTrade(context, TRAINER_2_JEWEL_SKILL_BOOKS_3, 3, 10);

        registerRandomPriceSellTrade(context, TRAINER_3_CRYSTAL_GOLD_JEWEL,
                ItemRegistry.CRYSTAL_GOLD, 3, 1, 2, 16, 10);

        registerRandomPriceSellTrade(context, TRAINER_3_HORSESHOE_GOLD_JEWEL,
                ItemRegistry.HORSESHOE_GOLD, 3, 1, 2, 16, 10);

        registerRandomPriceOrderTrade(context, TRAINER_3_JEWEL_SR_UMA_TICKET,
                ItemRegistry.SR_UMA_TICKET, 1, 1, 3, 12, 20);

        registerRandomPriceOrderTrade(context, TRAINER_3_JEWEL_SR_CARD_TICKET,
                ItemRegistry.SR_CARD_TICKET, 1, 1, 3, 12, 20);

        registerSkillBooksTrade(context, TRAINER_3_JEWEL_SKILL_BOOKS_1, 1, 20);
        registerSkillBooksTrade(context, TRAINER_3_JEWEL_SKILL_BOOKS_2, 2, 20);
        registerSkillBooksTrade(context, TRAINER_3_JEWEL_SKILL_BOOKS_3, 3, 20);

        registerRandomItemOrderTrade(context, TRAINER_4_JEWEL_TRAINING_ITEMS,
                List.of(ItemRegistry.SPEED_MID_ITEM,
                        ItemRegistry.STAMINA_MID_ITEM,
                        ItemRegistry.STRENGTH_MID_ITEM,
                        ItemRegistry.MENTALITY_MID_ITEM,
                        ItemRegistry.WISDOM_MID_ITEM),
                2, 2, 5, 6, 30);

        registerSkillBooksTrade(context, TRAINER_4_JEWEL_SKILL_BOOKS_1, 1, 30);
        registerSkillBooksTrade(context, TRAINER_4_JEWEL_SKILL_BOOKS_2, 2, 30);
        registerSkillBooksTrade(context, TRAINER_4_JEWEL_SKILL_BOOKS_3, 3, 30);

        registerRandomPriceSellTrade(context, TRAINER_5_CRYSTAL_RAINBOW_JEWEL,
                ItemRegistry.CRYSTAL_RAINBOW, 5, 1, 2, 16, 30, 0.2F);

        registerRandomPriceSellTrade(context, TRAINER_5_HORSESHOE_RAINBOW_JEWEL,
                ItemRegistry.HORSESHOE_RAINBOW, 5, 1, 2, 16, 30, 0.2F);

        registerRandomPriceOrderTrade(context, TRAINER_5_JEWEL_SSR_UMA_TICKET,
                ItemRegistry.SSR_UMA_TICKET, 2, 1, 2, 12, 30, 0.2F);

        registerRandomPriceOrderTrade(context, TRAINER_5_JEWEL_SSR_CARD_TICKET,
                ItemRegistry.SSR_CARD_TICKET, 2, 1, 2, 12, 30, 0.2F);

        registerSkillBooksTrade(context, TRAINER_5_JEWEL_SKILL_BOOKS_1, 1, 30);
        registerSkillBooksTrade(context, TRAINER_5_JEWEL_SKILL_BOOKS_2, 2, 30);
        registerSkillBooksTrade(context, TRAINER_5_JEWEL_SKILL_BOOKS_3, 3, 30);

        registerRandomItemOrderTrade(context, TRAINER_5_JEWEL_TRAINING_ITEMS,
                List.of(ItemRegistry.SPEED_HIGH_ITEM,
                        ItemRegistry.STAMINA_HIGH_ITEM,
                        ItemRegistry.STRENGTH_HIGH_ITEM,
                        ItemRegistry.MENTALITY_HIGH_ITEM,
                        ItemRegistry.WISDOM_HIGH_ITEM),
                2, 2, 5, 6, 30, 0.2F);

        registerRaceTicketsTrade(context, TRAINER_1_JEWEL_RACE_TICKETS, 1);
        registerRaceTicketsTrade(context, TRAINER_2_JEWEL_RACE_TICKETS, 2);
        registerRaceTicketsTrade(context, TRAINER_3_JEWEL_RACE_TICKETS, 3);
        registerRaceTicketsTrade(context, TRAINER_4_JEWEL_RACE_TICKETS, 4);
        registerRaceTicketsTrade(context, TRAINER_5_JEWEL_RACE_TICKETS, 5);
    }

    private static void registerWandererOffers(final BootstrapContext<VillagerTrade> context) {
        // Common trades
        registerRandomPriceOrderTrade(context, WANDERING_TRADER_JEWEL_HACHIMI_MID,
                ItemRegistry.HACHIMI_MID, 1, 1, 4, 32, 2);
        registerRandomPriceOrderTrade(context, WANDERING_TRADER_JEWEL_HACHIMI_BIG,
                ItemRegistry.HACHIMI_BIG, 2, 1, 4, 32, 2);
        registerRandomPriceOrderTrade(context, WANDERING_TRADER_JEWEL_SMALL_ENERGY_DRINK,
                ItemRegistry.SMALL_ENERGY_DRINK, 1, 1, 4, 32, 2);
        registerRandomPriceOrderTrade(context, WANDERING_TRADER_JEWEL_MEDIUM_ENERGY_DRINK,
                ItemRegistry.MEDIUM_ENERGY_DRINK, 2, 1, 4, 32, 2);

        // "Rare" trades
        registerRandomPriceOrderTrade(context, WANDERING_TRADER_JEWEL_LARGE_ENERGY_DRINK,
                ItemRegistry.LARGE_ENERGY_DRINK, 3, 1, 2, 32, 15);
        registerRandomPriceOrderTrade(context, WANDERING_TRADER_JEWEL_ROYAL_BITTER,
                ItemRegistry.ROYAL_BITTER, 2, 1, 4, 32, 15);
    }

    private static void registerRandomPriceOrderTrade(
            final BootstrapContext<VillagerTrade> context,
            ResourceKey<VillagerTrade> resourceKey,
            Item item,
            int pBaseEmeraldCost,
            int pMinCount,
            int pMaxCount,
            int pMaxUses,
            int pVillagerXp
    ) {
        registerRandomPriceOrderTrade(context, resourceKey, item, pBaseEmeraldCost, pMinCount, pMaxCount,
                pMaxUses, pVillagerXp, 0.05F);
    }

    private static void registerRandomPriceOrderTrade(
            final BootstrapContext<VillagerTrade> context,
            ResourceKey<VillagerTrade> resourceKey,
            Item item,
            int pBaseEmeraldCost,
            int pMinCount,
            int pMaxCount,
            int pMaxUses,
            int pVillagerXp,
            float pPriceMultiplier
    ) {
        registerRandomPriceOrderTrade(context, resourceKey, new ItemStackTemplate(item),
                pBaseEmeraldCost, pMinCount, pMaxCount, pMaxUses, pVillagerXp, pPriceMultiplier);
    }

    private static void registerRandomPriceOrderTrade(
            final BootstrapContext<VillagerTrade> context,
            ResourceKey<VillagerTrade> resourceKey,
            ItemStackTemplate item,
            int pBaseEmeraldCost,
            int pMinCount,
            int pMaxCount,
            int pMaxUses,
            int pVillagerXp,
            float pPriceMultiplier
    ) {
        pBaseEmeraldCost = Math.min(pBaseEmeraldCost + 5, 64);
        VillagerTrades.register(context, resourceKey,
                new VillagerTrade(
                        new TradeCost(ItemRegistry.JEWEL,
                                Sum.sum(
                                        ConstantValue.exactly(pBaseEmeraldCost),
                                        UniformGenerator.between(0.0f, Math.min(64 - pBaseEmeraldCost, 14))
                                )
                        ),
                        item,
                        pMaxUses, pVillagerXp, pPriceMultiplier, Optional.empty(),
                        List.of(SetItemCountFunction
                                .setCount(UniformGenerator.between(pMinCount, pMaxCount))
                                .build())
                )
        );
    }

    private static void registerRandomItemOrderTrade(
            final BootstrapContext<VillagerTrade> context,
            ResourceKey<VillagerTrade> resourceKey,
            List<Item> items,
            int pBaseEmeraldCost,
            int pMinCount,
            int pMaxCount,
            int pMaxUses,
            int pVillagerXp
    ) {
        registerRandomItemOrderTrade(context, resourceKey, items, pBaseEmeraldCost, pMinCount, pMaxCount,
                pMaxUses, pVillagerXp, 0.05F);
    }

    private static void registerRandomItemOrderTrade(
            final BootstrapContext<VillagerTrade> context,
            ResourceKey<VillagerTrade> resourceKey,
            List<Item> items,
            int pBaseEmeraldCost,
            int pMinCount,
            int pMaxCount,
            int pMaxUses,
            int pVillagerXp,
            float pPriceMultiplier
    ) {
        pBaseEmeraldCost = Math.min(pBaseEmeraldCost, 64);
        float probability = 1.0f / items.size();
        Stream<LootItemFunction> itemSetters = items.stream()
                .map(item -> new SetItemFunction(
                        List.of(LootItemRandomChanceCondition
                                .randomChance(probability)
                                .build()),
                        item.builtInRegistryHolder()
                ));
        List<LootItemFunction> givenItemModifiers = Stream
                .concat(
                        itemSetters,
                        Stream.of(SetItemCountFunction
                                .setCount(UniformGenerator.between(pMinCount, pMaxCount))
                                .build())
                )
                .toList();
        VillagerTrades.register(context, resourceKey,
                new VillagerTrade(
                        new TradeCost(ItemRegistry.JEWEL, pBaseEmeraldCost),
                        new ItemStackTemplate(items.getFirst()),
                        pMaxUses, pVillagerXp, pPriceMultiplier, Optional.empty(),
                        givenItemModifiers
                )
        );
    }

    private static void registerRandomPriceSellTrade(
            final BootstrapContext<VillagerTrade> context,
            ResourceKey<VillagerTrade> resourceKey,
            Item item,
            int pBaseEmeraldCost,
            int pMinCount,
            int pMaxCount,
            int pMaxUses,
            int pVillagerXp
    ) {
        registerRandomPriceSellTrade(context, resourceKey, item, pBaseEmeraldCost, pMinCount, pMaxCount,
                pMaxUses, pVillagerXp, 0.05F);
    }

    private static void registerRandomPriceSellTrade(
            final BootstrapContext<VillagerTrade> context,
            ResourceKey<VillagerTrade> resourceKey,
            Item item,
            int pBaseEmeraldCost,
            int pMinCount,
            int pMaxCount,
            int pMaxUses,
            int pVillagerXp,
            float pPriceMultiplier
    ) {
        pBaseEmeraldCost = Math.min(pBaseEmeraldCost, 64);
        VillagerTrades.register(context, resourceKey,
                new VillagerTrade(
                        new TradeCost(item, UniformGenerator.between(pMinCount, pMaxCount)),
                        new ItemStackTemplate(ItemRegistry.JEWEL, pBaseEmeraldCost),
                        pMaxUses, pVillagerXp, pPriceMultiplier, Optional.empty(), List.of()
                )
        );
    }

    private static void registerRaceTicketsTrade(
            final BootstrapContext<VillagerTrade> context,
            ResourceKey<VillagerTrade> resourceKey,
            int level
    ) {
        RaceRanking rk = RaceRanking.values()[level];
        int baseValPrice = 5 * level;

        VillagerTrades.register(context, resourceKey,
                new VillagerTrade(
                        new TradeCost(ItemRegistry.JEWEL,
                                Sum.sum(
                                        ConstantValue.exactly(baseValPrice),
                                        UniformGenerator.between(-2.0f, 2.0f)
                                )
                        ),
                        new ItemStackTemplate(ItemRegistry.UMA_RACE_TICKET),
                        12, 6 * level, 0.2f, Optional.empty(),
                        List.of(new RaceTicketRandomLootFunction( // exclude champions
                                rk, rk, false, Set.of(RaceRegistry.PREDICATE_CHAMPIONS)))
                )
        );
    }

    private static void registerSkillBooksTrade(
            final BootstrapContext<VillagerTrade> context,
            ResourceKey<VillagerTrade> resourceKey,
            int level,
            int pVillagerXp
    ) {
        int i = level * 2;
        int baseCost = Math.min(2 + 3 * i, 64);
        int randMax = Math.min(5 + i * 10 - 1, 64 - baseCost);

        VillagerTrades.register(context, resourceKey,
                new VillagerTrade(
                        new TradeCost(ItemRegistry.JEWEL,
                                Sum.sum(
                                        ConstantValue.exactly(baseCost),
                                        UniformGenerator.between(0.0f, randMax)
                                )
                        ),
                        new ItemStackTemplate(ItemRegistry.SKILL_BOOK),
                        12, pVillagerXp, 0.2F, Optional.empty(),
                        List.of(new UmaSkillLootFunction(List.of(), Optional.empty(), level))
                )
        );
    }
}

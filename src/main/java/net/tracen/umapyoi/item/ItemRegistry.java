package net.tracen.umapyoi.item;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.level.block.Block;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.item.factor.FactorReport;
import net.tracen.umapyoi.item.factor.FactorReport;
import net.tracen.umapyoi.item.factor.UmaFactorContainerItem;
import net.tracen.umapyoi.item.food.EnergyDrinkMethods;
import net.tracen.umapyoi.item.food.UmaDrinkItem;
import net.tracen.umapyoi.item.food.UmaFoodItem;
import net.tracen.umapyoi.item.info.FoodInfo;
import net.tracen.umapyoi.item.weapon.BaseballBatItem;
import net.tracen.umapyoi.item.weapon.GrassNaginataItem;
import net.tracen.umapyoi.registry.RegistryObject;
import net.tracen.umapyoi.registry.TrainingSupportRegistry;
import net.tracen.umapyoi.registry.training.SupportType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ItemRegistry {
    public static final List<Item> ITEMS = new ArrayList<>();

    public static final Item SILVER_UMA_PEDESTAL = registerBlock("silver_uma_pedestal",
            BlockRegistry.SILVER_UMA_PEDESTAL);
    
    public static final Item UMA_PEDESTAL = registerBlock("uma_pedestal",
            BlockRegistry.UMA_PEDESTAL);

    public static final Item UMA_STATUE = registerBlock("uma_statue",
            BlockRegistry.UMA_STATUES);
    
    public static final Item THREE_GODDESS = registerBlock("three_goddess",
            BlockRegistry.THREE_GODDESS);

    public static final Item TRAINING_FACILITY = registerBlock("training_facility",
            BlockRegistry.TRAINING_FACILITY);

    public static final Item SKILL_LEARNING_TABLE = registerBlock("skill_learning_table",
            BlockRegistry.SKILL_LEARNING_TABLE);

    public static final Item REGISTER_LECTERN = registerBlock("register_lectern",
            BlockRegistry.REGISTER_LECTERN);
    
    public static final Item DISASSEMBLY_BLOCK = registerBlock("disassembly_block",
            BlockRegistry.DISASSEMBLY_BLOCK);

    public static final Item UMA_SELECT_BLOCK = registerBlock("uma_select_block",
            BlockRegistry.UMA_SELECT_BLOCK);

    public static final RegistryObject<Item> RACE_SELECT_BLOCK = register("race_select_block",
            () -> new BlockItem(BlockRegistry.RACE_SELECT_BLOCK.get(), Umapyoi.defaultItemProperties()));

    public static final RegistryObject<Item> RACE_REGISTER_BLOCK = register("race_register",
            () -> new BlockItem(BlockRegistry.RACE_REGISTER_BLOCK.get(), Umapyoi.defaultItemProperties()));

    public static final RegistryObject<Item> FACTOR_DECOMPOSE_TABLE = register("factor_decompose_table",
            () -> new BlockItem(BlockRegistry.FACTOR_DECOMPOSE_TABLE.get(), Umapyoi.defaultItemProperties()));

    public static final RegistryObject<Item> FACTOR_RESEARCH_TABLE = register("factor_research_table",
            () -> new BlockItem(BlockRegistry.FACTOR_RESEARCH_TABLE.get(), Umapyoi.defaultItemProperties()));

    public static final RegistryObject<Item> GATE_DOOR = register("gate_door", () -> new BlockItem(BlockRegistry.GATE_DOOR.get(),
            Umapyoi.defaultItemProperties()));

    public static final RegistryObject<Item> GATE = register("gate", () -> new BlockItem(BlockRegistry.GATE.get(), Umapyoi.defaultItemProperties()));

    public static final Item BLANK_UMA_SOUL = registerItem("blank_uma_soul",
            FadedUmaSoulItem::new,
            Umapyoi.defaultItemProperties().stacksTo(1));
    public static final Item UMA_SOUL_DISPLAY = registerItem("uma_soul_display");
    public static final Item UMA_SOUL = registerItem("uma_soul",
            UmaSoulItem::new,
            Umapyoi.defaultItemProperties().stacksTo(1));
    public static final Item UMA_FACTOR_ITEM = registerItem("uma_factor_item",
            UmaFactorContainerItem::new,
            Umapyoi.defaultItemProperties().stacksTo(1));

    public static final Item SUMMER_UNIFORM = registerItem("summer_uniform",
            SummerUniformItem::new,
            Umapyoi.defaultItemProperties().stacksTo(1));
    public static final Item WINTER_UNIFORM = registerItem("winter_uniform",
            WinterUniformItem::new,
            Umapyoi.defaultItemProperties().stacksTo(1));
    public static final Item TRAINING_SUIT = registerItem("trainning_suit",
            TrainingSuitItem::new,
            Umapyoi.defaultItemProperties().stacksTo(1));
    public static final Item SWIMSUIT = registerItem("swimsuit",
            SwimsuitItem::new,
            Umapyoi.defaultItemProperties().stacksTo(1));

    public static final Item UMA_COSTUME = registerItem("uma_costume",
            UmaCostumeItem::new,
            Umapyoi.defaultItemProperties().stacksTo(1));

    public static final Item JEWEL = registerItem("jewel");

    public static final Item BLANK_TICKET = registerItem("blank_ticket");
    public static final Item UMA_TICKET = registerItem("uma_ticket", UmaTicketItem::new);
    public static final Item SR_UMA_TICKET = registerItem("sr_uma_ticket", UmaTicketItem::new);
    public static final Item SSR_UMA_TICKET = registerItem("ssr_uma_ticket", UmaTicketItem::new);
    public static final Item CARD_TICKET = registerItem("card_ticket", UmaTicketItem::new);
    public static final Item SR_CARD_TICKET = registerItem("sr_card_ticket", UmaTicketItem::new);
    public static final Item SSR_CARD_TICKET = registerItem("ssr_card_ticket", UmaTicketItem::new);
    
    public static final Item CRYSTAL_SILVER = registerItem("crystal_silver");
    public static final Item CRYSTAL_GOLD = registerItem("crystal_gold");
    public static final Item CRYSTAL_RAINBOW = registerItem("crystal_rainbow");
    public static final Item HORSESHOE_SILVER = registerItem("horseshoe_silver");
    public static final Item HORSESHOE_GOLD = registerItem("horseshoe_gold");
    public static final Item HORSESHOE_RAINBOW = registerItem("horseshoe_rainbow");

    public static final Item SPEED_LOW_ITEM = registerItem("speed_low_item",
            p -> new TrainingItem(SupportType.SPEED, TrainingSupportRegistry.SPEED_SUPPORT, 1, p));
    public static final Item SPEED_MID_ITEM = registerItem("speed_mid_item",
            p -> new TrainingItem(SupportType.SPEED, TrainingSupportRegistry.SPEED_SUPPORT, 2, p));
    public static final Item SPEED_HIGH_ITEM = registerItem("speed_high_item",
            p -> new TrainingItem(SupportType.SPEED, TrainingSupportRegistry.SPEED_SUPPORT, 3, p));

    public static final Item STAMINA_LOW_ITEM = registerItem("stamina_low_item",
            p -> new TrainingItem(SupportType.STAMINA, TrainingSupportRegistry.STAMINA_SUPPORT, 1, p));
    public static final Item STAMINA_MID_ITEM = registerItem("stamina_mid_item",
            p -> new TrainingItem(SupportType.STAMINA, TrainingSupportRegistry.STAMINA_SUPPORT, 2, p));
    public static final Item STAMINA_HIGH_ITEM = registerItem("stamina_high_item",
            p -> new TrainingItem(SupportType.STAMINA, TrainingSupportRegistry.STAMINA_SUPPORT, 3, p));

    public static final Item STRENGTH_LOW_ITEM = registerItem("strength_low_item",
            p -> new TrainingItem(SupportType.STRENGTH, TrainingSupportRegistry.STRENGTH_SUPPORT, 1, p));
    public static final Item STRENGTH_MID_ITEM = registerItem("strength_mid_item",
            p -> new TrainingItem(SupportType.STRENGTH, TrainingSupportRegistry.STRENGTH_SUPPORT, 2, p));
    public static final Item STRENGTH_HIGH_ITEM = registerItem("strength_high_item",
            p -> new TrainingItem(SupportType.STRENGTH, TrainingSupportRegistry.STRENGTH_SUPPORT, 3, p));

    public static final Item MENTALITY_LOW_ITEM = registerItem("mentality_low_item",
            p -> new TrainingItem(SupportType.GUTS, TrainingSupportRegistry.GUTS_SUPPORT, 1, p));
    public static final Item MENTALITY_MID_ITEM = registerItem("mentality_mid_item",
            p -> new TrainingItem(SupportType.GUTS, TrainingSupportRegistry.GUTS_SUPPORT, 2, p));
    public static final Item MENTALITY_HIGH_ITEM = registerItem("mentality_high_item",
            p -> new TrainingItem(SupportType.GUTS, TrainingSupportRegistry.GUTS_SUPPORT, 3, p));

    public static final Item WISDOM_LOW_ITEM = registerItem("wisdom_low_item",
            p -> new TrainingItem(SupportType.WISDOM, TrainingSupportRegistry.WISDOM_SUPPORT, 1, p));
    public static final Item WISDOM_MID_ITEM = registerItem("wisdom_mid_item",
            p -> new TrainingItem(SupportType.WISDOM, TrainingSupportRegistry.WISDOM_SUPPORT, 2, p));
    public static final Item WISDOM_HIGH_ITEM = registerItem("wisdom_high_item",
            p -> new TrainingItem(SupportType.WISDOM, TrainingSupportRegistry.WISDOM_SUPPORT, 3, p));

    public static final Item SKILL_BOOK = registerItem("skill_book",
            SkillBookItem::new,
            Umapyoi.defaultItemProperties().stacksTo(1));

    public static final Item SUPPORT_CARD = registerItem("support_card",
            SupportCardItem::new,
            Umapyoi.defaultItemProperties()
                    .stacksTo(1)
                    .component(DataComponents.REPAIRABLE, new Repairable(HolderSet.direct(
                            ItemRegistry.HORSESHOE_GOLD.builtInRegistryHolder(),
                            ItemRegistry.HORSESHOE_SILVER.builtInRegistryHolder(),
                            ItemRegistry.HORSESHOE_RAINBOW.builtInRegistryHolder()
                    ))));

    public static final RegistryObject<Item> UMA_RACE_TICKET = register("race_ticket", UmaRaceTicketItem::new);

    public static final Item HACHIMI_MID = registerItem("hachimi_mid",
            p -> new UmaDrinkItem(p, e -> {},
                    FoodInfo.builder().name("hachimi_mid").alwaysEat().amountAndCalories(2, 0.6F).water(30F)
                            .nutrients(2F, 2F, 0F, 0F, 0F).decayModifier(1.0F).heatCapacity(1F).cookingTemp(480F)
                            .build()));

    public static final Item HACHIMI_BIG = registerItem("hachimi_big", p -> new UmaDrinkItem(p, e -> {},
            FoodInfo.builder().name("hachimi_big").alwaysEat().amountAndCalories(4, 0.8F).water(60F)
                    .nutrients(4F, 4F, 0F, 0F, 0F).decayModifier(1.0F).heatCapacity(1F).cookingTemp(480F).build()));

    public static final Item ROYAL_BITTER = registerItem("royal_bitter",
            p -> new UmaDrinkItem(p, EnergyDrinkMethods::royalBitter,
                    FoodInfo.builder().name("royal_bitter").alwaysEat().amountAndCalories(2, 0.6F).water(50F)
                            .nutrients(0F, 2F, 2F, 0F, 0F)
                            .heatCapacity(1F).cookingTemp(480F).build()));

    public static final Item CUPCAKE = registerItem("cupcake",
            p -> new UmaFoodItem(p, e -> {},
                    FoodInfo.builder().name("cupcake").amountAndCalories(5, 0.6F).water(0F)
                            .nutrients(2F, 2F, 2F, 0F, 2F).decayModifier(1.5F).heatCapacity(1F).cookingTemp(480F)
                            .build()));

    public static final Item SWEET_CUPCAKE = registerItem("sweet_cupcake", p -> new UmaFoodItem(p, e -> {},
            FoodInfo.builder().name("sweet_cupcake").amountAndCalories(7, 0.6F).water(0F).nutrients(4F, 4F, 2F, 0F, 4F)
                    .decayModifier(1.5F).heatCapacity(1F).cookingTemp(480F).build()));

    public static final Item SMALL_ENERGY_DRINK = registerItem("small_energy_drink",
            p -> new UmaDrinkItem(p, EnergyDrinkMethods::smallEnergy,
                    FoodInfo.builder().name("small_energy_drink").alwaysEat().amountAndCalories(2, 0.6F).water(30F)
                            .nutrients(0F, 1F, 1F, 0F, 0F)
                            .heatCapacity(1F).cookingTemp(480F).build()));

    public static final Item MEDIUM_ENERGY_DRINK = registerItem("medium_energy_drink",
            p -> new UmaDrinkItem(p, EnergyDrinkMethods::mediumEnergy,
                    FoodInfo.builder().name("medium_energy_drink").alwaysEat().amountAndCalories(2, 0.6F).water(50F)
                            .nutrients(0F, 2F, 2F, 0F, 0F)
                            .heatCapacity(1F).cookingTemp(480F).build()));

    public static final Item LARGE_ENERGY_DRINK = registerItem("large_energy_drink",
            p -> new UmaDrinkItem(p, EnergyDrinkMethods::largeEnergy,
                    FoodInfo.builder().name("large_energy_drink").alwaysEat().amountAndCalories(2, 0.6F).water(70F)
                            .nutrients(0F, 3F, 3F, 0F, 0F)
                            .heatCapacity(1F).cookingTemp(480F).build()));

    public static final Item NAGINATA = registerItem("naginata",
            GrassNaginataItem::new,
            GrassNaginataItem.createProperties());

    public static final Item BASEBALL_BAT = registerItem("baseball_bat",
            BaseballBatItem::new,
            BaseballBatItem.createProperties());

    public static final RegistryObject<Item> FACTOR_SHARD = register("uma_factor_shard", FactorReport::new);

    // dummy manual item to load the model for modonomicon
    public static final Item MANUAL_CLOSED = registerItem("manual_closed");

    private static ResourceKey<Item> modItemId(String name) {
        return ResourceKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, name));
    }

    public static Item registerBlock(String name, Block block) {
        return registerBlock(name, block, BlockItem::new);
    }

    public static Item registerBlock(String name, Block block, Item.Properties properties) {
        return registerBlock(name, block, BlockItem::new, properties);
    }

    public static Item registerBlock(String name, Block block, BiFunction<Block, Item.Properties, Item> factory) {
        return registerBlock(modItemId(name), block, factory, Umapyoi.defaultItemProperties());
    }

    public static Item registerBlock(String name, Block block, BiFunction<Block, Item.Properties, Item> factory, Item.Properties properties) {
        return registerItem(modItemId(name), (propertiesx) -> (Item)factory.apply(block, propertiesx), properties.useBlockDescriptionPrefix());
    }

    public static Item registerBlock(ResourceKey<Item> key, Block block, BiFunction<Block, Item.Properties, Item> factory) {
        return registerBlock(key, block, factory, Umapyoi.defaultItemProperties());
    }

    public static Item registerBlock(ResourceKey<Item> key, Block block, BiFunction<Block, Item.Properties, Item> factory, Item.Properties properties) {
        return registerItem(key, (propertiesx) -> (Item)factory.apply(block, propertiesx), properties.useBlockDescriptionPrefix());
    }

    public static Item registerItem(String name, Function<Item.Properties, Item> factory) {
        return registerItem(modItemId(name), factory, Umapyoi.defaultItemProperties());
    }

    public static Item registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        return registerItem(modItemId(name), factory, properties);
    }

    public static Item registerItem(String name, Item.Properties properties) {
        return registerItem(modItemId(name), Item::new, properties);
    }

    public static Item registerItem(String name) {
        return registerItem(modItemId(name), Item::new, Umapyoi.defaultItemProperties());
    }

    public static Item registerItem(ResourceKey<Item> key, Function<Item.Properties, Item> factory) {
        return registerItem(key, factory, Umapyoi.defaultItemProperties());
    }

    public static Item registerItem(ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties properties) {
        Item item = factory.apply(properties.setId(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }

        ITEMS.add(item);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }
}

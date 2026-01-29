package net.tracen.umapyoi.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.data.tag.UmapyoiItemTags;
import net.tracen.umapyoi.item.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class UmapyoiRecipeProvider extends FabricRecipeProvider {
    public UmapyoiRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    @MethodsReturnNonnullByDefault
    public RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                shapeless(RecipeCategory.MISC, ItemRegistry.JEWEL)
                        .requires(Items.CARROT)
                        .requires(Ingredient.of(
                                Items.DIAMOND,
                                Items.EMERALD
                        ))
                        .unlockedBy("has_item", has(Items.CARROT)).save(output);

                shapeless(RecipeCategory.MISC, ItemRegistry.BLANK_TICKET, 2).requires(Items.PAPER).requires(Items.PAPER)
                        .requires(Items.LAPIS_LAZULI).requires(ItemRegistry.JEWEL)
                        .unlockedBy("has_item", has(ItemRegistry.JEWEL))
                        .save(output);
                shapeless(RecipeCategory.MISC, ItemRegistry.UMA_TICKET).requires(ItemRegistry.BLANK_TICKET)
                        .requires(ItemRegistry.CRYSTAL_SILVER)
                        .unlockedBy("has_item", has(ItemRegistry.BLANK_TICKET)).save(output);
                shapeless(RecipeCategory.MISC, ItemRegistry.SR_UMA_TICKET).requires(ItemRegistry.BLANK_TICKET)
                        .requires(ItemRegistry.CRYSTAL_GOLD).unlockedBy("has_item", has(ItemRegistry.BLANK_TICKET))
                        .save(output);
                shapeless(RecipeCategory.MISC, ItemRegistry.SSR_UMA_TICKET).requires(ItemRegistry.BLANK_TICKET)
                        .requires(ItemRegistry.CRYSTAL_RAINBOW)
                        .unlockedBy("has_item", has(ItemRegistry.BLANK_TICKET)).save(output);

                shapeless(RecipeCategory.MISC, ItemRegistry.CARD_TICKET).requires(ItemRegistry.BLANK_TICKET)
                        .requires(ItemRegistry.HORSESHOE_SILVER)
                        .unlockedBy("has_item", has(ItemRegistry.BLANK_TICKET)).save(output);
                shapeless(RecipeCategory.MISC, ItemRegistry.SR_CARD_TICKET).requires(ItemRegistry.BLANK_TICKET)
                        .requires(ItemRegistry.HORSESHOE_GOLD)
                        .unlockedBy("has_item", has(ItemRegistry.BLANK_TICKET)).save(output);
                shapeless(RecipeCategory.MISC, ItemRegistry.SSR_CARD_TICKET).requires(ItemRegistry.BLANK_TICKET)
                        .requires(ItemRegistry.HORSESHOE_RAINBOW)
                        .unlockedBy("has_item", has(ItemRegistry.BLANK_TICKET)).save(output);

                shaped(RecipeCategory.DECORATIONS, BlockRegistry.DISASSEMBLY_BLOCK)
                        .pattern(" J ")
                        .pattern(" A ")
                        .pattern("ALA")
                        .define('A', Items.IRON_INGOT)
                        .define('L', Items.LECTERN)
                        .define('J', ItemRegistry.BLANK_TICKET)
                        .unlockedBy("has_item", has(ItemRegistry.BLANK_TICKET)).save(output);

                shaped(RecipeCategory.COMBAT, ItemRegistry.NAGINATA)
                        .pattern("  J")
                        .pattern(" L ")
                        .pattern("A  ")
                        .define('A', Items.STICK)
                        .define('L', Items.LIGHTNING_ROD)
                        .define('J', ItemRegistry.HORSESHOE_RAINBOW)
                        .unlockedBy("has_item", has(ItemRegistry.HORSESHOE_RAINBOW)).save(output);

                shaped(RecipeCategory.COMBAT, ItemRegistry.BASEBALL_BAT)
                        .pattern("  A")
                        .pattern("JA ")
                        .pattern("L  ")
                        .define('A', Items.IRON_BLOCK)
                        .define('L', Items.IRON_INGOT)
                        .define('J', ItemRegistry.HORSESHOE_SILVER)
                        .unlockedBy("has_item", has(ItemRegistry.HORSESHOE_RAINBOW)).save(output);

                shaped(RecipeCategory.DECORATIONS, BlockRegistry.UMA_SELECT_BLOCK).pattern(" J ").pattern("BLB").pattern("AAA")
                        .define('A', Items.DIAMOND)
                        .define('B', Items.NETHER_STAR).define('L', Items.LECTERN)
                        .define('J', ItemRegistry.JEWEL)
                        .unlockedBy("has_item", has(ItemRegistry.BLANK_TICKET)).save(output);
                shaped(RecipeCategory.DECORATIONS, BlockRegistry.THREE_GODDESS).pattern(" J ")
                        .pattern("JAJ").pattern("AAA").define('A', ConventionalItemTags.STONES)
                        .define('J', ItemRegistry.JEWEL)
                        .unlockedBy("has_item", has(ItemRegistry.JEWEL)).save(output);

                shaped(RecipeCategory.MISC, BlockRegistry.REGISTER_LECTERN).pattern(" J ").pattern(" G ").pattern("GAG")
                        .define('A', Items.LECTERN).define('G', Items.GOLD_INGOT).define('J', ItemRegistry.JEWEL)
                        .unlockedBy("has_item", has(ItemRegistry.JEWEL)).save(output);

                shaped(RecipeCategory.MISC, BlockRegistry.SILVER_UMA_PEDESTAL).pattern("AJA").pattern("GAG").pattern("AAA")
                        .define('A', ConventionalItemTags.STONES).define('G', Items.IRON_INGOT).define('J', ItemRegistry.JEWEL)
                        .unlockedBy("has_item", has(ItemRegistry.JEWEL)).save(output);

                shaped(RecipeCategory.MISC, BlockRegistry.UMA_PEDESTAL).pattern(" J ").pattern("GAG").pattern("GGG")
                        .define('A', ItemRegistry.SILVER_UMA_PEDESTAL).define('G', Items.GOLD_INGOT)
                        .define('J', Ingredient.of(
                                ItemRegistry.CRYSTAL_GOLD,
                                ItemRegistry.HORSESHOE_GOLD
                        ))
                        .unlockedBy("has_item", has(ItemRegistry.CRYSTAL_GOLD)).save(output);

                shaped(RecipeCategory.DECORATIONS,
                                BlockRegistry.UMA_STATUES).pattern(" J ").pattern(" A ").pattern("AAA")
                        .define('A', ConventionalItemTags.STONES)
                        .define('J', ItemRegistry.JEWEL).unlockedBy("has_item", has(ItemRegistry.JEWEL))
                        .save(output);

                shaped(RecipeCategory.COMBAT, ItemRegistry.SWIMSUIT)
                        .pattern("IJI")
                        .pattern("ILI")
                        .pattern(" I ")
                        .define('I', Items.LEATHER)
                        .define('L', Items.BLUE_DYE)
                        .define('J', ItemRegistry.JEWEL)
                        .unlockedBy("has_item", has(ItemRegistry.JEWEL)).save(output);

                shaped(RecipeCategory.MISC, BlockRegistry.SKILL_LEARNING_TABLE).pattern(" J").pattern(" L")
                        .define('L', Items.BOOKSHELF).define('J', ItemRegistry.JEWEL)
                        .unlockedBy("has_item", has(ItemRegistry.JEWEL)).save(output);

                shaped(RecipeCategory.MISC, BlockRegistry.TRAINING_FACILITY).pattern("IJI").pattern("ILI")
                        .define('I', Items.IRON_INGOT).define('L', Items.CRAFTING_TABLE)
                        .define('J', ItemRegistry.JEWEL).unlockedBy("has_item", has(ItemRegistry.JEWEL))
                        .save(output);

                shaped(RecipeCategory.COMBAT, ItemRegistry.SUMMER_UNIFORM).pattern("IJI").pattern("ILI").pattern("ILI")
                        .define('I', Items.PURPLE_WOOL).define('L', Items.WHITE_WOOL).define('J', ItemRegistry.JEWEL)
                        .unlockedBy("has_item", has(ItemRegistry.JEWEL)).save(output);

                shaped(RecipeCategory.COMBAT, ItemRegistry.WINTER_UNIFORM).pattern("IJI").pattern("III").pattern("III")
                        .define('I', Items.PURPLE_WOOL).define('J', ItemRegistry.JEWEL)
                        .unlockedBy("has_item", has(ItemRegistry.JEWEL)).save(output);

                shaped(RecipeCategory.COMBAT, ItemRegistry.TRAINING_SUIT).pattern("IJI").pattern("ILI").pattern("ILI")
                        .define('I', Items.RED_WOOL).define('L', Items.WHITE_WOOL).define('J', ItemRegistry.JEWEL)
                        .unlockedBy("has_item", has(ItemRegistry.JEWEL)).save(output);

                shapeless(RecipeCategory.FOOD, ItemRegistry.HACHIMI_MID).requires(Items.HONEY_BOTTLE)
                        .requires(UmapyoiItemTags.SUGAR).requires(UmapyoiItemTags.SUGAR).requires(ConventionalItemTags.WATER_BUCKETS)
                        .unlockedBy("has_item", has(Items.HONEY_BOTTLE)).save(output);

                shapeless(RecipeCategory.FOOD, ItemRegistry.HACHIMI_BIG).requires(Items.HONEY_BOTTLE)
                        .requires(Items.HONEY_BOTTLE).requires(UmapyoiItemTags.SUGAR).requires(UmapyoiItemTags.SUGAR)
                        .requires(ConventionalItemTags.WATER_BUCKETS).unlockedBy("has_item", has(Items.HONEY_BLOCK)).save(output);

                shapeless(RecipeCategory.FOOD, ItemRegistry.SMALL_ENERGY_DRINK).requires(Items.CARROT)
                        .requires(Items.NETHER_WART).requires(UmapyoiItemTags.SUGAR).requires(ConventionalItemTags.WATER_BUCKETS)
                        .unlockedBy("has_item", has(Items.NETHER_WART)).save(output);

                shapeless(RecipeCategory.FOOD, ItemRegistry.MEDIUM_ENERGY_DRINK).requires(Items.CARROT)
                        .requires(Items.CARROT).requires(Items.REDSTONE)
                        .requires(Items.NETHER_WART).requires(UmapyoiItemTags.SUGAR).requires(ConventionalItemTags.WATER_BUCKETS)
                        .unlockedBy("has_item", has(Items.NETHER_WART)).save(output);

                shapeless(RecipeCategory.FOOD, ItemRegistry.LARGE_ENERGY_DRINK).requires(Items.CARROT)
                        .requires(Items.CARROT).requires(Items.REDSTONE)
                        .requires(Items.GLOWSTONE_DUST).requires(Items.NETHER_WART)
                        .requires(UmapyoiItemTags.SUGAR).requires(ConventionalItemTags.WATER_BUCKETS)
                        .unlockedBy("has_item", has(Items.NETHER_WART)).save(output);

                shapeless(RecipeCategory.FOOD, ItemRegistry.ROYAL_BITTER)
                        .requires(Items.WHEAT_SEEDS).requires(Items.GLISTERING_MELON_SLICE)
                        .requires(Items.REDSTONE).requires(Items.NETHER_WART)
                        .requires(ConventionalItemTags.WATER_BUCKETS)
                        .unlockedBy("has_item", has(Items.NETHER_WART)).save(output);

                shapeless(RecipeCategory.FOOD, ItemRegistry.CUPCAKE).requires(Items.CARROT)
                        .requires(Items.WHEAT).requires(Items.EGG).requires(UmapyoiItemTags.SUGAR)
                        .requires(ConventionalItemTags.MILK_BUCKETS).unlockedBy("has_item", has(Items.CARROT)).save(output);

                shapeless(RecipeCategory.FOOD, ItemRegistry.SWEET_CUPCAKE).requires(Items.CARROT)
                        .requires(Items.CARROT).requires(Items.WHEAT).requires(Items.EGG)
                        .requires(UmapyoiItemTags.SUGAR).requires(UmapyoiItemTags.SUGAR).requires(ConventionalItemTags.MILK_BUCKETS)
                        .unlockedBy("has_item", has(Items.CARROT)).save(output);
            }
        };
    }

    @Override
    @MethodsReturnNonnullByDefault
    public String getName() {
        return "UmapyoiRecipeProvider";
    }
}

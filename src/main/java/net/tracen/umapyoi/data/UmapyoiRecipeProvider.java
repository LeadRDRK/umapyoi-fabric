package net.tracen.umapyoi.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.data.tag.UmapyoiItemTags;
import net.tracen.umapyoi.item.ItemRegistry;

import java.util.function.Consumer;

public class UmapyoiRecipeProvider extends FabricRecipeProvider {
    public UmapyoiRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.JEWEL.get())
                .requires(Items.CARROT)
                .requires(Ingredient.of(
                        Items.DIAMOND,
                        Items.EMERALD
                ))
                .unlockedBy("has_item", has(Items.CARROT)).save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.BLANK_TICKET.get(), 2).requires(Items.PAPER).requires(Items.PAPER)
                .requires(Items.LAPIS_LAZULI).requires(ItemRegistry.JEWEL.get())
                .unlockedBy("has_item", has(ItemRegistry.JEWEL.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.UMA_TICKET.get()).requires(ItemRegistry.BLANK_TICKET.get())
                .requires(ItemRegistry.CRYSTAL_SILVER.get())
                .unlockedBy("has_item", has(ItemRegistry.BLANK_TICKET.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.SR_UMA_TICKET.get()).requires(ItemRegistry.BLANK_TICKET.get())
                .requires(ItemRegistry.CRYSTAL_GOLD.get()).unlockedBy("has_item", has(ItemRegistry.BLANK_TICKET.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.SSR_UMA_TICKET.get()).requires(ItemRegistry.BLANK_TICKET.get())
                .requires(ItemRegistry.CRYSTAL_RAINBOW.get())
                .unlockedBy("has_item", has(ItemRegistry.BLANK_TICKET.get())).save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.CARD_TICKET.get()).requires(ItemRegistry.BLANK_TICKET.get())
                .requires(ItemRegistry.HORSESHOE_SILVER.get())
                .unlockedBy("has_item", has(ItemRegistry.BLANK_TICKET.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.SR_CARD_TICKET.get()).requires(ItemRegistry.BLANK_TICKET.get())
                .requires(ItemRegistry.HORSESHOE_GOLD.get())
                .unlockedBy("has_item", has(ItemRegistry.BLANK_TICKET.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.SSR_CARD_TICKET.get()).requires(ItemRegistry.BLANK_TICKET.get())
                .requires(ItemRegistry.HORSESHOE_RAINBOW.get())
                .unlockedBy("has_item", has(ItemRegistry.BLANK_TICKET.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.DISASSEMBLY_BLOCK.get()).pattern(" J ").pattern("ALA").pattern("AAA")
                .define('A', Items.IRON_INGOT).define('L', Items.LECTERN)
                .define('J', ItemRegistry.BLANK_TICKET.get())
                .unlockedBy("has_item", has(ItemRegistry.BLANK_TICKET.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.THREE_GODDESS.get()).pattern(" J ").pattern("JLJ").pattern("AAA")
                .define('A', Items.STONE).define('L', Items.QUARTZ_BLOCK)
                .define('J', ItemRegistry.JEWEL.get()).unlockedBy("has_item", has(ItemRegistry.JEWEL.get()))
                .save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.UMA_STATUES.get()).pattern(" J ").pattern(" A ").pattern("AAA")
                .define('A', Items.STONE)
                .define('J', ItemRegistry.JEWEL.get()).unlockedBy("has_item", has(ItemRegistry.JEWEL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.REGISTER_LECTERN.get()).pattern(" J ").pattern(" G ").pattern("GAG")
                .define('A', Items.LECTERN).define('G', Items.GOLD_INGOT).define('J', ItemRegistry.JEWEL.get())
                .unlockedBy("has_item", has(ItemRegistry.JEWEL.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.SILVER_UMA_PEDESTAL.get()).pattern("AJA").pattern("GAG").pattern("AAA")
                .define('A', Items.STONE).define('G', Items.IRON_INGOT).define('J', ItemRegistry.JEWEL.get())
                .unlockedBy("has_item", has(ItemRegistry.JEWEL.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.UMA_PEDESTAL.get()).pattern(" J ").pattern("GAG").pattern("GGG")
                .define('A', ItemRegistry.SILVER_UMA_PEDESTAL.get()).define('G', Items.GOLD_INGOT)
                .define('J', Ingredient.of(
                        ItemRegistry.CRYSTAL_GOLD.get(),
                        ItemRegistry.HORSESHOE_GOLD.get()
                ))
                .unlockedBy("has_item", has(ItemRegistry.CRYSTAL_GOLD.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.SKILL_LEARNING_TABLE.get()).pattern(" J").pattern(" L")
                .define('L', Items.BOOKSHELF).define('J', ItemRegistry.JEWEL.get())
                .unlockedBy("has_item", has(ItemRegistry.JEWEL.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.TRAINING_FACILITY.get()).pattern("IJI").pattern("ILI")
                .define('I', Items.IRON_INGOT).define('L', Items.CRAFTING_TABLE)
                .define('J', ItemRegistry.JEWEL.get()).unlockedBy("has_item", has(ItemRegistry.JEWEL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.SUMMER_UNIFORM.get()).pattern("IJI").pattern("ILI").pattern("ILI")
                .define('I', Items.PURPLE_WOOL).define('L', Items.WHITE_WOOL).define('J', ItemRegistry.JEWEL.get())
                .unlockedBy("has_item", has(ItemRegistry.JEWEL.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.WINTER_UNIFORM.get()).pattern("IJI").pattern("III").pattern("III")
                .define('I', Items.PURPLE_WOOL).define('J', ItemRegistry.JEWEL.get())
                .unlockedBy("has_item", has(ItemRegistry.JEWEL.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.TRAINING_SUIT.get()).pattern("IJI").pattern("ILI").pattern("ILI")
                .define('I', Items.RED_WOOL).define('L', Items.WHITE_WOOL).define('J', ItemRegistry.JEWEL.get())
                .unlockedBy("has_item", has(ItemRegistry.JEWEL.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.KINDERGARTEN_UNIFORM.get()).pattern("IJI").pattern("III").pattern("III")
                .define('I', Items.LIGHT_BLUE_WOOL).define('J', ItemRegistry.JEWEL.get())
                .unlockedBy("has_item", has(ItemRegistry.JEWEL.get())).save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemRegistry.HACHIMI_MID.get()).requires(Items.HONEY_BOTTLE)
                .requires(UmapyoiItemTags.SUGAR).requires(UmapyoiItemTags.SUGAR).requires(UmapyoiItemTags.WATER)
                .unlockedBy("has_item", has(Items.HONEY_BOTTLE)).save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemRegistry.HACHIMI_BIG.get()).requires(Items.HONEY_BOTTLE)
                .requires(Items.HONEY_BOTTLE).requires(UmapyoiItemTags.SUGAR).requires(UmapyoiItemTags.SUGAR)
                .requires(UmapyoiItemTags.WATER).unlockedBy("has_item", has(Items.HONEY_BLOCK)).save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemRegistry.SMALL_ENERGY_DRINK.get()).requires(Items.CARROT)
                .requires(Items.NETHER_WART).requires(UmapyoiItemTags.SUGAR).requires(UmapyoiItemTags.WATER)
                .unlockedBy("has_item", has(Items.NETHER_WART)).save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemRegistry.MEDIUM_ENERGY_DRINK.get()).requires(Items.CARROT)
                .requires(Items.CARROT).requires(Items.REDSTONE)
                .requires(Items.NETHER_WART).requires(UmapyoiItemTags.SUGAR).requires(UmapyoiItemTags.WATER)
                .unlockedBy("has_item", has(Items.NETHER_WART)).save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemRegistry.LARGE_ENERGY_DRINK.get()).requires(Items.CARROT)
                .requires(Items.CARROT).requires(Items.REDSTONE)
                .requires(Items.GLOWSTONE_DUST).requires(Items.NETHER_WART)
                .requires(UmapyoiItemTags.SUGAR).requires(UmapyoiItemTags.WATER)
                .unlockedBy("has_item", has(Items.NETHER_WART)).save(consumer);
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemRegistry.ROYAL_BITTER.get())
                .requires(Items.WHEAT_SEEDS).requires(Items.GLISTERING_MELON_SLICE)
                .requires(Items.REDSTONE).requires(Items.NETHER_WART)
                .requires(UmapyoiItemTags.WATER)
                .unlockedBy("has_item", has(Items.NETHER_WART)).save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemRegistry.CUPCAKE.get()).requires(Items.CARROT)
                .requires(Items.WHEAT).requires(Items.EGG).requires(UmapyoiItemTags.SUGAR)
                .requires(UmapyoiItemTags.MILK).unlockedBy("has_item", has(Items.CARROT)).save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemRegistry.SWEET_CUPCAKE.get()).requires(Items.CARROT)
                .requires(Items.CARROT).requires(Items.WHEAT).requires(Items.EGG)
                .requires(UmapyoiItemTags.SUGAR).requires(UmapyoiItemTags.SUGAR).requires(UmapyoiItemTags.MILK)
                .unlockedBy("has_item", has(Items.CARROT)).save(consumer);
        
    }
}

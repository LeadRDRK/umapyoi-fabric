package net.tracen.umapyoi.compat.jei;

import com.google.common.collect.Lists;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.compat.jei.category.JEIDisassemblyCategory;
import net.tracen.umapyoi.compat.jei.category.JEIGachaCategory;
import net.tracen.umapyoi.compat.jei.recipes.JEISimpleRecipe;
import net.tracen.umapyoi.compat.jei.recipes.UmapyoiJEIRecipes;
import net.tracen.umapyoi.data.tag.UmapyoiItemTags;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.utils.GachaRanking;

import java.util.List;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    public static final Identifier PLUGIN_ID = Identifier.fromNamespaceAndPath(Umapyoi.MODID, "jei_plugin");

    public static final IRecipeType<JEISimpleRecipe> GACHA_JEI_TYPE = IRecipeType
            .create(Umapyoi.MODID, "gacha_recipe", JEISimpleRecipe.class);

    public static final IRecipeType<JEISimpleRecipe> DISASSEMBLY_JEI_TYPE = IRecipeType
            .create(Umapyoi.MODID, "disassembly_recipe", JEISimpleRecipe.class);

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JEIGachaCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new JEIDisassemblyCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(DISASSEMBLY_JEI_TYPE, getAllDisassemblyRecipes());
        registration.addRecipes(GACHA_JEI_TYPE, getAllGachaRecipes());
    }

    private List<JEISimpleRecipe> getAllDisassemblyRecipes() {
        List<JEISimpleRecipe> result = Lists.newArrayList();
        result.add(UmapyoiJEIRecipes.disassembleUmasoul(ItemRegistry.CRYSTAL_SILVER.getDefaultInstance(), GachaRanking.R));
        result.add(UmapyoiJEIRecipes.disassembleUmasoul(ItemRegistry.CRYSTAL_GOLD.getDefaultInstance(), GachaRanking.SR));
        result.add(UmapyoiJEIRecipes.disassembleUmasoul(ItemRegistry.CRYSTAL_RAINBOW.getDefaultInstance(), GachaRanking.SSR));
        
        result.add(UmapyoiJEIRecipes.disassembleSupportCard(ItemRegistry.HORSESHOE_SILVER.getDefaultInstance(), GachaRanking.R));
        result.add(UmapyoiJEIRecipes.disassembleSupportCard(ItemRegistry.HORSESHOE_GOLD.getDefaultInstance(), GachaRanking.SR));
        result.add(UmapyoiJEIRecipes.disassembleSupportCard(ItemRegistry.HORSESHOE_RAINBOW.getDefaultInstance(), GachaRanking.SSR));
        return result;
    }

    private static Ingredient ingredientOfTag(TagKey<Item> tagKey) {
        return Ingredient.of(BuiltInRegistries.ITEM.get(tagKey).orElseThrow());
    }

    private List<JEISimpleRecipe> getAllGachaRecipes() {
        return Lists.newArrayList(
                UmapyoiJEIRecipes.gachaUmasoul(ingredientOfTag(UmapyoiItemTags.COMMON_GACHA_ITEM), GachaRanking.R),
                UmapyoiJEIRecipes.gachaUmasoul(
                        DifferenceIngredient.of(
                                DifferenceIngredient.of(
                                        DifferenceIngredient.of(ingredientOfTag(UmapyoiItemTags.UMA_TICKET),
                                                ingredientOfTag(UmapyoiItemTags.SSR_UMA_TICKET)).toVanilla(),
                                        ingredientOfTag(UmapyoiItemTags.SR_UMA_TICKET)).toVanilla(),
                                ingredientOfTag(UmapyoiItemTags.COMMON_GACHA_ITEM)).toVanilla(),
                        GachaRanking.R, GachaRanking.SR, GachaRanking.SSR),
                UmapyoiJEIRecipes.gachaUmasoul(ingredientOfTag(UmapyoiItemTags.SR_UMA_TICKET), GachaRanking.SR,
                        GachaRanking.SSR),
                UmapyoiJEIRecipes.gachaUmasoul(ingredientOfTag(UmapyoiItemTags.SSR_UMA_TICKET), GachaRanking.SSR),

                UmapyoiJEIRecipes.gachaSupportCard(ingredientOfTag(UmapyoiItemTags.COMMON_GACHA_ITEM), GachaRanking.R),
                UmapyoiJEIRecipes.gachaSupportCard(
                        DifferenceIngredient.of(
                                DifferenceIngredient.of(
                                        DifferenceIngredient.of(ingredientOfTag(UmapyoiItemTags.CARD_TICKET),
                                                ingredientOfTag(UmapyoiItemTags.SSR_CARD_TICKET)).toVanilla(),
                                        ingredientOfTag(UmapyoiItemTags.SR_CARD_TICKET)).toVanilla(),
                                ingredientOfTag(UmapyoiItemTags.COMMON_GACHA_ITEM)).toVanilla(),
                        GachaRanking.R, GachaRanking.SR, GachaRanking.SSR),
                UmapyoiJEIRecipes.gachaSupportCard(ingredientOfTag(UmapyoiItemTags.SR_CARD_TICKET), GachaRanking.SR,
                        GachaRanking.SSR),
                UmapyoiJEIRecipes.gachaSupportCard(ingredientOfTag(UmapyoiItemTags.SSR_CARD_TICKET), GachaRanking.SSR));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(DISASSEMBLY_JEI_TYPE, ItemRegistry.DISASSEMBLY_BLOCK.getDefaultInstance());

        registration.addCraftingStation(GACHA_JEI_TYPE, ItemRegistry.UMA_PEDESTAL.getDefaultInstance());
        registration.addCraftingStation(GACHA_JEI_TYPE, ItemRegistry.SILVER_UMA_PEDESTAL.getDefaultInstance());
    }

    @Override
    public Identifier getPluginUid() {
        return PLUGIN_ID;
    }

}

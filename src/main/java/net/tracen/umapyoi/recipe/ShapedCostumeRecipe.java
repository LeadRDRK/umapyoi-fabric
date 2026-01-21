package net.tracen.umapyoi.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;

import java.util.Optional;

public class ShapedCostumeRecipe extends ShapedRecipe {

    public static final RecipeSerializer<ShapedCostumeRecipe> SERIALIZER = new CostumeRecipeSerializer<>(
            RecipeSerializer.SHAPED_RECIPE, ShapedCostumeRecipe::new);

    private final ResourceLocation output;

    public ShapedCostumeRecipe(ShapedRecipe compose, ResourceLocation outputBlade) {
        super(compose.getGroup(), compose.category(),
                new ShapedRecipePattern(compose.getWidth(), compose.getHeight(), compose.getIngredients(), Optional.empty()),
                getResultItem(outputBlade));
        this.output = outputBlade;
    }

    private static ItemStack getResultItem(ResourceLocation output) {
        Item bladeItem = BuiltInRegistries.ITEM.containsKey(output) ? BuiltInRegistries.ITEM.get(output)
                : ItemRegistry.UMA_COSTUME.get();

        return bladeItem.getDefaultInstance();
    }

    public ResourceLocation getOutput() {
        return output;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return this.getResultItem(registries).copy();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        ItemStack result = ShapedCostumeRecipe.getResultItem(output).copy();
        if (!BuiltInRegistries.ITEM.getKey(result.getItem()).equals(getOutput())) {
            result = ItemRegistry.UMA_COSTUME.get().getDefaultInstance();
            result.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), output);
        }
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

}

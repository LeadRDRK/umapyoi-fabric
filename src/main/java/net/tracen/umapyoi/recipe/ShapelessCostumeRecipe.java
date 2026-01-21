package net.tracen.umapyoi.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;

public class ShapelessCostumeRecipe extends ShapelessRecipe {

    public static final RecipeSerializer<ShapelessCostumeRecipe> SERIALIZER = new CostumeRecipeSerializer<>(
            RecipeSerializer.SHAPELESS_RECIPE, ShapelessCostumeRecipe::new);

    private final ResourceLocation output;

    public ShapelessCostumeRecipe(ShapelessRecipe compose, ResourceLocation output) {
        super(compose.getGroup(), compose.category(),
                getResultItem(output), compose.getIngredients());
        this.output = output;
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
        ItemStack result = ShapelessCostumeRecipe.getResultItem(output).copy();
        if(registries == RegistryAccess.EMPTY)
            return result;

        if (!BuiltInRegistries.ITEM.getKey(result.getItem()).equals(output)) {
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

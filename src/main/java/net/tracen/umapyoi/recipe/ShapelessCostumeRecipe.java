package net.tracen.umapyoi.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;

public class ShapelessCostumeRecipe extends ShapelessRecipe {

    public static final RecipeSerializer<ShapelessRecipe> SERIALIZER = new CostumeRecipeSerializer<>(
            RecipeSerializer.SHAPELESS_RECIPE, ShapelessCostumeRecipe::new);

    private final Identifier output;

    public ShapelessCostumeRecipe(ShapelessRecipe compose, Identifier output) {
        super(compose.group(), compose.category(),
                getResultItem(output), compose.placementInfo().ingredients());
        this.output = output;
    }

    private static ItemStack getResultItem(Identifier output) {
        Item bladeItem = BuiltInRegistries.ITEM.containsKey(output)
                ? BuiltInRegistries.ITEM.get(output).orElseThrow().value()
                : ItemRegistry.UMA_COSTUME;

        return bladeItem.getDefaultInstance();
    }

    public Identifier getOutput() {
        return output;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack result = ShapelessCostumeRecipe.getResultItem(output).copy();
        if(registries == RegistryAccess.EMPTY)
            return result;

        if (!BuiltInRegistries.ITEM.getKey(result.getItem()).equals(output)) {
            result = ItemRegistry.UMA_COSTUME.getDefaultInstance();
            result.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), output);
        }
        return result;
    }

    @Override
    public RecipeSerializer<ShapelessRecipe> getSerializer() {
        return SERIALIZER;
    }

}

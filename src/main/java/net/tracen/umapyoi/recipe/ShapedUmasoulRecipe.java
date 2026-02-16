package net.tracen.umapyoi.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.tracen.umapyoi.item.FadedUmaSoulItem;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.umadata.UmaData;

import java.util.Optional;

public class ShapedUmasoulRecipe extends ShapedRecipe {

    public static final RecipeSerializer<ShapedRecipe> SERIALIZER = new UmasoulRecipeSerializer<>(
            RecipeSerializer.SHAPED_RECIPE, ShapedUmasoulRecipe::new);

    private final Identifier outputUma;

    public ShapedUmasoulRecipe(ShapedRecipe compose, Identifier outputBlade) {
        super(compose.group(), compose.category(),
                new ShapedRecipePattern(compose.getWidth(), compose.getHeight(), compose.getIngredients(), Optional.empty()),
                getResultItem(outputBlade));
        this.outputUma = outputBlade;
    }

    private static ItemStack getResultItem(Identifier outputBlade) {
        Item bladeItem = BuiltInRegistries.ITEM.containsKey(outputBlade)
                ? BuiltInRegistries.ITEM.get(outputBlade).orElseThrow().value()
                : ItemRegistry.BLANK_UMA_SOUL;

        return bladeItem.getDefaultInstance();
    }

    public Identifier getOutputUma() {
        return outputUma;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack result = getResultItem(outputUma).copy();
        if(registries == RegistryAccess.EMPTY)
            return result;

        if (!BuiltInRegistries.ITEM.getKey(result.getItem()).equals(outputUma)) {
            var dataOpt = registries
                    .lookupOrThrow(UmaData.REGISTRY_KEY)
                    .get(ResourceKey.create(UmaData.REGISTRY_KEY, outputUma));
            if (dataOpt.isEmpty())
                return ItemStack.EMPTY;

            var data = dataOpt.get().value();
            return FadedUmaSoulItem.genUmaSoul(outputUma, data);
        }
        return result;
    }

    @Override
    public RecipeSerializer<ShapedRecipe> getSerializer() {
        return SERIALIZER;
    }

}

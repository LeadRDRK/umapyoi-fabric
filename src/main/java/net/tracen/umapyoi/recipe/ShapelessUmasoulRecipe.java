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
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.tracen.umapyoi.item.FadedUmaSoulItem;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.umadata.UmaData;

public class ShapelessUmasoulRecipe extends ShapelessRecipe {

    public static final RecipeSerializer<ShapelessRecipe> SERIALIZER = new UmasoulRecipeSerializer<>(
            RecipeSerializer.SHAPELESS_RECIPE, ShapelessUmasoulRecipe::new);

    private final Identifier outputUma;

    public ShapelessUmasoulRecipe(ShapelessRecipe compose, Identifier output) {
        super(compose.group(), compose.category(),
                getResultItem(output), compose.ingredients);
        this.outputUma = output;
    }

    private static ItemStack getResultItem(Identifier output) {
        Item bladeItem = BuiltInRegistries.ITEM.containsKey(output)
                ? BuiltInRegistries.ITEM.get(output).orElseThrow().value()
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
    public RecipeSerializer<ShapelessRecipe> getSerializer() {
        return SERIALIZER;
    }

}

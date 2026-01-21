package net.tracen.umapyoi.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.tracen.umapyoi.item.FadedUmaSoulItem;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.umadata.UmaData;

public class ShapelessUmasoulRecipe extends ShapelessRecipe {

    public static final RecipeSerializer<ShapelessUmasoulRecipe> SERIALIZER = new UmasoulRecipeSerializer<>(
            RecipeSerializer.SHAPELESS_RECIPE, ShapelessUmasoulRecipe::new);

    private final ResourceLocation outputUma;

    public ShapelessUmasoulRecipe(ShapelessRecipe compose, ResourceLocation output) {
        super(compose.getGroup(), compose.category(),
                getResultItem(output), compose.getIngredients());
        this.outputUma = output;
    }

    private static ItemStack getResultItem(ResourceLocation output) {
        Item bladeItem = BuiltInRegistries.ITEM.containsKey(output) ? BuiltInRegistries.ITEM.get(output)
                : ItemRegistry.BLANK_UMA_SOUL.get();

        return bladeItem.getDefaultInstance();
    }

    public ResourceLocation getOutputUma() {
        return outputUma;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return this.getResultItem(registries).copy();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
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
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

}

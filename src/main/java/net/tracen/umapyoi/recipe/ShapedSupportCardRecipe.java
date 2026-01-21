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
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.training.card.SupportCard;

import java.util.Optional;

public class ShapedSupportCardRecipe extends ShapedRecipe {

    public static final RecipeSerializer<ShapedSupportCardRecipe> SERIALIZER = new SupportCardRecipeSerializer<>(
            RecipeSerializer.SHAPED_RECIPE, ShapedSupportCardRecipe::new);

    private final ResourceLocation outputUma;

    public ShapedSupportCardRecipe(ShapedRecipe compose, ResourceLocation outputBlade) {
        super(compose.getGroup(), compose.category(),
                new ShapedRecipePattern(compose.getWidth(), compose.getHeight(), compose.getIngredients(), Optional.empty()),
                getResultItem(outputBlade));
        this.outputUma = outputBlade;
    }

    private static ItemStack getResultItem(ResourceLocation outputBlade) {
        Item bladeItem = BuiltInRegistries.ITEM.containsKey(outputBlade) ? BuiltInRegistries.ITEM.get(outputBlade)
                : ItemRegistry.SUPPORT_CARD.get();

        return bladeItem.getDefaultInstance();
    }

    public ResourceLocation getOutput() {
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
            var supportCardOpt = registries
                    .lookupOrThrow(SupportCard.REGISTRY_KEY)
                    .get(ResourceKey.create(SupportCard.REGISTRY_KEY, outputUma));
            if (supportCardOpt.isEmpty())
                return ItemStack.EMPTY;

            var supportCard = supportCardOpt.get().value();
            return SupportCard.init(outputUma, supportCard);
        }
        return result;
    }
    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

}

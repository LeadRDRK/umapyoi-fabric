package net.tracen.umapyoi.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.training.card.SupportCard;

public class ShapelessSupportCardRecipe extends ShapelessRecipe {

    public static final RecipeSerializer<ShapelessSupportCardRecipe> SERIALIZER = new SupportCardRecipeSerializer<>(
            RecipeSerializer.SHAPELESS_RECIPE, ShapelessSupportCardRecipe::new);

    private final ResourceLocation outputUma;

    public ShapelessSupportCardRecipe(ShapelessRecipe compose, ResourceLocation outputBlade) {
        super(compose.getGroup(), compose.category(),
                getResultItem(outputBlade), compose.getIngredients());
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
    public ItemStack assemble(CraftingContainer craftingContainer, HolderLookup.Provider registries) {
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

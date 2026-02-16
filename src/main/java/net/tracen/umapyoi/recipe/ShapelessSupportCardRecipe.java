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
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.training.card.SupportCard;

public class ShapelessSupportCardRecipe extends ShapelessRecipe {

    public static final RecipeSerializer<ShapelessRecipe> SERIALIZER = new SupportCardRecipeSerializer<>(
            RecipeSerializer.SHAPELESS_RECIPE, ShapelessSupportCardRecipe::new);

    private final Identifier outputUma;

    public ShapelessSupportCardRecipe(ShapelessRecipe compose, Identifier outputBlade) {
        super(compose.group(), compose.category(),
                getResultItem(outputBlade), compose.placementInfo().ingredients());
        this.outputUma = outputBlade;
    }

    private static ItemStack getResultItem(Identifier outputBlade) {
        Item bladeItem = BuiltInRegistries.ITEM.containsKey(outputBlade)
                ? BuiltInRegistries.ITEM.get(outputBlade).orElseThrow().value()
                : ItemRegistry.SUPPORT_CARD;

        return bladeItem.getDefaultInstance();
    }

    public Identifier getOutput() {
        return outputUma;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
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
    public RecipeSerializer<ShapelessRecipe> getSerializer() {
        return SERIALIZER;
    }

}

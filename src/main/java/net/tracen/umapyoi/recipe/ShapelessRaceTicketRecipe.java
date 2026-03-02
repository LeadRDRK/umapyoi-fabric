package net.tracen.umapyoi.recipe;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.races.Race;

public class ShapelessRaceTicketRecipe extends ShapelessRecipe {
    public static final RecipeSerializer<ShapelessRaceTicketRecipe> SERIALIZER = new RaceTicketRecipeSerializer<>(
            RecipeSerializer.SHAPELESS_RECIPE, ShapelessRaceTicketRecipe::new
    );

    private final ResourceLocation baseItemOrKey;
    public ShapelessRaceTicketRecipe(ShapelessRecipe compose, ResourceLocation loc) {
        super(compose.getGroup(), compose.category(),
                getResultItem(loc), compose.getIngredients());
        this.baseItemOrKey = loc;
    }

    private static ItemStack getResultItem(ResourceLocation loc) {
        return (BuiltInRegistries.ITEM.containsKey(loc) ?
                BuiltInRegistries.ITEM.get(loc) :
                ItemRegistry.UMA_RACE_TICKET.get()).getDefaultInstance();
    }

    public ResourceLocation getKey() { return this.baseItemOrKey; }

    @Override
    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        return this.getResultItem(pRegistryAccess).copy();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        ItemStack result = getResultItem(this.baseItemOrKey).copy();
        if (pRegistryAccess == RegistryAccess.EMPTY) return result;
        Registry<Race> registry = pRegistryAccess.registryOrThrow(Race.REGISTRY_KEY);
        if (!BuiltInRegistries.ITEM.getKey(result.getItem()).equals(this.baseItemOrKey))
            result = ItemRegistry.UMA_RACE_TICKET.get().getDefaultInstance();
        if (registry.containsKey(this.baseItemOrKey)) {
            result.getOrCreateTag().putString("race", this.baseItemOrKey.toString());
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}

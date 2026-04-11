package net.tracen.umapyoi.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.UmaRaceTicketItem;
import net.tracen.umapyoi.registry.races.Race;

import org.jetbrains.annotations.Nullable;

public class ShapelessRaceTicketRecipe extends ShapelessRecipe implements RaceTicketRecipe<CraftingInput> {
    public static final RecipeSerializer<ShapelessRecipe> SERIALIZER = new RaceTicketRecipeSerializer<>(
            RecipeSerializer.SHAPELESS_RECIPE, ShapelessRaceTicketRecipe::new
    );

    private final ResourceLocation baseItemOrKey;
    public ShapelessRaceTicketRecipe(ShapelessRecipe compose, ResourceLocation loc) {
        super(compose.group(), compose.category(),
                getResultItem(loc), compose.ingredients);
        this.baseItemOrKey = loc;
    }

    private static ItemStack getResultItem(ResourceLocation loc) {
        return BuiltInRegistries.ITEM.get(loc)
                .map(Holder::value)
                .orElse(ItemRegistry.UMA_RACE_TICKET)
                .getDefaultInstance();
    }

    @Override
    public ResourceLocation getKey() { return this.baseItemOrKey; }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack result = getResultItem(this.baseItemOrKey).copy();
        if(registries == RegistryAccess.EMPTY)
            return result;

        if (!BuiltInRegistries.ITEM.getKey(result.getItem()).equals(this.baseItemOrKey)) {
            var race = UmapyoiAPI.getRaceRegistry(registries)
                    .get(ResourceKey.create(Race.REGISTRY_KEY, this.baseItemOrKey))
                    .map(Holder::value)
                    .orElse(null);
            result = UmaRaceTicketItem.init(this.baseItemOrKey, race);
        }
        return result;
    }

    @Override
    public RecipeSerializer<ShapelessRecipe> getSerializer() {
        return SERIALIZER;
    }

    public record ComposeOutput(RecipeOutput compose, ResourceLocation raceId) implements RecipeOutput {
        @Override
        public void accept(ResourceKey<Recipe<?>> key, Recipe<?> recipe, @Nullable AdvancementHolder advancement) {
            var composeRecipe = new ShapelessRaceTicketRecipe((ShapelessRecipe) recipe, raceId);
            compose.accept(key, composeRecipe, advancement);
        }

        @Override
        public Advancement.Builder advancement() {
            return compose.advancement();
        }

        @Override
        public void includeRootAdvancement() {
            compose.includeRootAdvancement();
        }
    }
}

package net.tracen.umapyoi.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;

import org.jetbrains.annotations.Nullable;

public class ShapelessRaceTicketRecipe extends ShapelessRecipe implements RaceTicketRecipe<CraftingInput> {
    public static final RecipeSerializer<ShapelessRecipe> SERIALIZER = new RaceTicketRecipeSerializerFactory<>(
            ShapelessRecipe.SERIALIZER, ShapelessRaceTicketRecipe::new
    ).createAsCompose();

    private final Identifier baseItemOrKey;
    public ShapelessRaceTicketRecipe(ShapelessRecipe compose, Identifier loc) {
        super(
                new CommonInfo(compose.showNotification()),
                new CraftingBookInfo(compose.category(), compose.group()),
                getResultItem(loc),
                compose.ingredients
        );
        this.baseItemOrKey = loc;
    }

    private static ItemStackTemplate getResultItem(Identifier output) {
        if (BuiltInRegistries.ITEM.containsKey(output)) {
            return new ItemStackTemplate(BuiltInRegistries.ITEM.get(output).orElseThrow().value());
        }
        else {
            return new ItemStackTemplate(ItemRegistry.UMA_RACE_TICKET,
                    DataComponentPatch.builder()
                            .set(DataComponentsTypeRegistry.DATA_LOCATION.get(), output)
                            .build());
        }
    }

    @Override
    public Identifier getKey() { return this.baseItemOrKey; }

    @Override
    public RecipeSerializer<ShapelessRecipe> getSerializer() {
        return SERIALIZER;
    }

    public record ComposeOutput(RecipeOutput compose, Identifier raceId) implements RecipeOutput {
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

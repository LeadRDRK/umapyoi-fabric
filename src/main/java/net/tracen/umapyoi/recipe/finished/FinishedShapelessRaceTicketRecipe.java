package net.tracen.umapyoi.recipe.finished;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.recipe.RecipeSerializerRegistry;

import java.util.List;

import javax.annotation.Nullable;

public record FinishedShapelessRaceTicketRecipe(ResourceLocation id, List<Ingredient> ingredients,
                                                ResourceLocation race) implements FinishedRecipe {
    @Nullable
    @Override
    public AdvancementHolder advancement() {
        return null;
    }

    @Override
    public RecipeSerializer<?> type() {
        return RecipeSerializerRegistry.SHAPELESS_RACE_TICKET.get();
    }

    @Override
    public void serializeRecipeData(JsonObject pJson) {
        JsonArray ingredientsArray = new JsonArray();
        this.ingredients.forEach(i -> ingredientsArray.add(i.toJson(false)));
        pJson.add("ingredients", ingredientsArray);

        JsonObject resultJson = new JsonObject();
        resultJson.addProperty("item", ItemRegistry.UMA_RACE_TICKET.getId().toString());
        resultJson.addProperty("count", 1);
        pJson.add("result", resultJson);

        if (race != null) {
            pJson.addProperty("race", race().toString());
        }
    }
}

package net.tracen.umapyoi.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

public interface RaceTicketRecipe<T extends RecipeInput> extends Recipe<T> {
    ResourceLocation getKey();
}

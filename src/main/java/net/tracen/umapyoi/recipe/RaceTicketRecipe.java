package net.tracen.umapyoi.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public interface RaceTicketRecipe<T extends Container> extends Recipe<T> {
    ResourceLocation getKey();
}

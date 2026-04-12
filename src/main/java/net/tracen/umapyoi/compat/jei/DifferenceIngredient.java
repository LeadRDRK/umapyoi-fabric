package net.tracen.umapyoi.compat.jei;

import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.stream.Stream;

import it.unimi.dsi.fastutil.ints.IntList;

public class DifferenceIngredient implements CustomIngredient {
    private final Ingredient base;
    private final Ingredient subtracted;
    private List<Holder<Item>> filteredMatchingItems;
    private IntList packedMatchingStacks;

    public static DifferenceIngredient of(Ingredient base, Ingredient subtracted) {
        return new DifferenceIngredient(base, subtracted);
    }

    private DifferenceIngredient(Ingredient base, Ingredient subtracted) {
        this.base = base;
        this.subtracted = subtracted;
    }

    @Override
    public boolean test(ItemStack stack) {
        if (stack == null || stack.isEmpty())
            return false;
        return base.test(stack) && !subtracted.test(stack);
    }

    @Override
    public Stream<Holder<Item>> items() {
        if (this.filteredMatchingItems == null)
            this.filteredMatchingItems = base.items()
                    .filter(holder -> !subtracted.test(new ItemStack(holder.value())))
                    .toList();
        return filteredMatchingItems.stream();
    }

    @Override
    public boolean requiresTesting() {
        return true;
    }

    @Override
    public CustomIngredientSerializer<?> getSerializer() {
        return null;
    }
}
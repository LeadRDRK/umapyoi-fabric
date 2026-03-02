package net.tracen.umapyoi.container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import javax.annotation.Nullable;

public class ExtendedResultContainer implements Container, RecipeHolder {
    public final int size;
    protected final NonNullList<ItemStack> itemStacks;

    @Nullable
    private Recipe<?> recipeUsed;

    public ExtendedResultContainer(int size) {
        this.itemStacks = NonNullList.withSize(size, ItemStack.EMPTY);
        this.size = size;
    }

    @Override
    public int getContainerSize() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.itemStacks.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return this.itemStacks.get(pIndex);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return ContainerHelper.takeItem(this.itemStacks, pIndex);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return ContainerHelper.takeItem(this.itemStacks, pIndex);
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        this.itemStacks.set(pIndex, pStack);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void clearContent() {
        this.itemStacks.clear();
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> pRecipe) {
        this.recipeUsed = pRecipe;
    }

    @Override
    @Nullable
    public Recipe<?> getRecipeUsed() {
        return this.recipeUsed;
    }
}

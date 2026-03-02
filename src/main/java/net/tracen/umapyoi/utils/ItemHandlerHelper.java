package net.tracen.umapyoi.utils;

import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemHandlerHelper {
    public static boolean canItemStacksStack(@Nonnull ItemStack a, @Nonnull ItemStack b)
    {
        if (a.isEmpty() || !ItemStack.isSameItem(a, b) || a.hasTag() != b.hasTag())
            return false;

        return (!a.hasTag() || a.getTag().equals(b.getTag()));
    }

    public static ItemStack copyStackWithSize(ItemStack itemStack, int size)
    {
        if (size == 0)
            return ItemStack.EMPTY;
        ItemStack copy = itemStack.copy();
        copy.setCount(size);
        return copy;
    }
}

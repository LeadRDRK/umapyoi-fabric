package net.tracen.umapyoi.utils;

import net.minecraft.world.item.ItemStack;

public class ItemHandlerHelper {
    public static ItemStack copyStackWithSize(ItemStack itemStack, int size)
    {
        if (size == 0)
            return ItemStack.EMPTY;
        ItemStack copy = itemStack.copy();
        copy.setCount(size);
        return copy;
    }
}

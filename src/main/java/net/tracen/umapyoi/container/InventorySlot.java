package net.tracen.umapyoi.container;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.block.entity.ImplementedInventory;

public class InventorySlot extends Slot {
    public InventorySlot(ImplementedInventory container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        var inventory = (ImplementedInventory) container;
        return inventory.isItemValid(getContainerSlot(), stack);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        var inventory = (ImplementedInventory) container;
        return inventory.getSlotLimit(getContainerSlot());
    }
}

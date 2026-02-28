package net.tracen.umapyoi.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.tracen.umapyoi.item.AbstractSuitItem;
import net.tracen.umapyoi.item.ItemRegistry;

public class UmaStatueBlockEntity extends SyncedInventoryEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private boolean isValid = false;

    public UmaStatueBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.UMA_STATUES.get(), pos, state);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        clearContent();
        ContainerHelper.loadAllItems(compound, items);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        ContainerHelper.saveAllItems(compound, items);
    }

    public boolean addItem(ItemStack itemStack) {
        if (isEmpty() && itemStack.is(ItemRegistry.UMA_SOUL.get())) {
            setItem(0, itemStack.split(1));
            setChanged();
            return true;
        }
        if (isCostumeEmpty() && !isEmpty() && itemStack.getItem() instanceof AbstractSuitItem) {
            setItem(1, itemStack.split(1));
            setChanged();
            return true;
        }
        return false;
    }

    public ItemStack removeItem() {
        if (!isCostumeEmpty()) {
            ItemStack item = getCostume().split(1);
            setChanged();
            return item;
        }
        if (!isEmpty()) {
            ItemStack item = getStoredItem().split(1);
            setChanged();
            return item;
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getStoredItem() {
        return items.get(0);
    }

    public boolean isEmpty() {
        return items.get(0).isEmpty();
    }

    public ItemStack getCostume() {
        return items.get(1);
    }

    public boolean isCostumeEmpty() {
        return items.get(1).isEmpty();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        isValid = false;
    }

    @Override
    public boolean stillValid(Player player) {
        return isValid;
    }
}

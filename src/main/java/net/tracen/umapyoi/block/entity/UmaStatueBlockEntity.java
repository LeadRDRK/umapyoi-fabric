package net.tracen.umapyoi.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.tracen.umapyoi.item.ItemRegistry;

public class UmaStatueBlockEntity extends SyncedInventoryEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private boolean isValid = false;

    public UmaStatueBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.UMA_STATUES.get(), pos, state);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        clearContent();
        ContainerHelper.loadAllItems(compound, items, registries);
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        ContainerHelper.saveAllItems(compound, items, registries);
    }

    public boolean addItem(ItemStack itemStack) {
        if (isEmpty() && itemStack.is(ItemRegistry.UMA_SOUL)) {
            setItem(0, itemStack.split(1));
            setChanged();
            return true;
        }
        return false;
    }

    public ItemStack removeItem() {
        if (!isEmpty()) {
            ItemStack item = removeItem(0, 1);
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

package net.tracen.umapyoi.container;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.block.entity.ThreeGoddessBlockEntity;

import java.util.Objects;

public class ThreeGoddessContainer extends AbstractContainerMenu {
    public final ThreeGoddessBlockEntity tileEntity;
    private final ContainerData containerData;
    private final ContainerLevelAccess canInteractWithCallable;

    public ThreeGoddessContainer(final int windowId, final Inventory playerInventory, BlockPos pos) {
        this(windowId, playerInventory, getTileEntity(playerInventory, pos), new SimpleContainerData(4));
    }

    public ThreeGoddessContainer(final int windowId, final Inventory playerInventory,
                                 final ThreeGoddessBlockEntity tileEntity, ContainerData dataIn) {
        super(ContainerRegistry.THREE_GODDESS.get(), windowId);
        this.tileEntity = tileEntity;
        this.containerData = dataIn;
        this.canInteractWithCallable = ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos());
        int startX = 8;

        this.addSlot(new InventorySlot(tileEntity, 0, 80, 27));
        this.addSlot(new InventorySlot(tileEntity, 1, 50, 78));
        this.addSlot(new InventorySlot(tileEntity, 2, 109, 78));

        this.addSlot(new CommonResultSlot(playerInventory.player, tileEntity, 3, 80, 79));

        // Main Player Inventory
        int startPlayerInvY = 137;
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column, startX + (column * 18),
                        startPlayerInvY + (row * 18)));
            }
        }

        // Hotbar
        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(playerInventory, column, startX + (column * 18), 195));
        }

        this.addDataSlots(dataIn);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();

            if (index >= 0 && index <= 3) {
                if (!this.moveItemStackTo(itemStack1, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack1, itemStack);
            } else if (index >= 4) {
                if (index >= 4 && index < 40) {
                    if (!this.moveItemStackTo(itemStack1, 0, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            
            if (itemStack1.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemStack1);
        }

        return itemStack;
    }

    private static ThreeGoddessBlockEntity getTileEntity(final Inventory playerInventory, BlockPos pos) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        final Player player = playerInventory.player;
        final BlockEntity tileAtPos = player.level().getBlockEntity(pos);
        if (tileAtPos instanceof ThreeGoddessBlockEntity) {
            return (ThreeGoddessBlockEntity) tileAtPos;
        }
        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(canInteractWithCallable, playerIn, BlockRegistry.THREE_GODDESS);
    }

    @Environment(EnvType.CLIENT)
    public int getProgressionScaled() {
        int i = this.containerData.get(0);
        return i != 0 ? i * 158 / ThreeGoddessBlockEntity.MAX_PROCESS_TIME : 0;
    }
}

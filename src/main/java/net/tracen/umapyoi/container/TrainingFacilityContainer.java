package net.tracen.umapyoi.container;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
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
import net.tracen.umapyoi.block.entity.TrainingFacilityBlockEntity;

import java.util.Objects;

public class TrainingFacilityContainer extends AbstractContainerMenu {
    public final TrainingFacilityBlockEntity tileEntity;
    private final ContainerData containerData;
    private final ContainerLevelAccess canInteractWithCallable;

    public TrainingFacilityContainer(final int windowId, final Inventory playerInventory, FriendlyByteBuf buf) {
        this(windowId, playerInventory, getTileEntity(playerInventory, buf), new SimpleContainerData(4));
    }

    public TrainingFacilityContainer(final int windowId, final Inventory playerInventory,
                                     final TrainingFacilityBlockEntity tileEntity, ContainerData cookingPotDataIn) {
        super(ContainerRegistry.TRAINING_FACILITY.get(), windowId);
        this.tileEntity = tileEntity;
        this.containerData = cookingPotDataIn;
        this.canInteractWithCallable = ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos());
        int startX = 8;

        this.addSlot(new InventorySlot(tileEntity, 0, 80, 98));
        for (int i = 1; i < 4; i++) {
            this.addSlot(new InventorySlot(tileEntity, i, (i - 1) * 27 + 12, 19));
        }

        for (int i = 4; i < 7; i++) {
            this.addSlot(new InventorySlot(tileEntity, i, (i - 4) * 27 + 94, 19));
        }

        // Main Player Inventory
        int startPlayerInvY = 120;
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column, startX + (column * 18),
                        startPlayerInvY + (row * 18)));
            }
        }

        // Hotbar
        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(playerInventory, column, startX + (column * 18), 178));
        }

        this.addDataSlots(cookingPotDataIn);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        // 0-7: Contain inventory
        // 7-33: Player inventory
        // 33-43: Hot bar in the player inventory

        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();

            if (index >= 0 && index <= 6) {
                if (!this.moveItemStackTo(itemStack1, 7, 43, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack1, itemStack);
            } else if (index >= 7 && index < 43) {
                if (!this.moveItemStackTo(itemStack1, 0, 7, false)) {
                    return ItemStack.EMPTY;
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

    private static TrainingFacilityBlockEntity getTileEntity(final Inventory playerInventory, FriendlyByteBuf data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        final Player player = playerInventory.player;
        final BlockEntity tileAtPos = player.level().getBlockEntity(data.readBlockPos());
        if (tileAtPos instanceof TrainingFacilityBlockEntity) {
            return (TrainingFacilityBlockEntity) tileAtPos;
        }
        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(canInteractWithCallable, playerIn, BlockRegistry.TRAINING_FACILITY.get());
    }

    @Environment(EnvType.CLIENT)
    public int getProgressionScaled() {
        int i = this.containerData.get(0);
        return i != 0 ? i * 130 / TrainingFacilityBlockEntity.MAX_PROCESS_TIME : 0;
    }

    @Environment(EnvType.CLIENT)
    public int getAnimation() {
        int i = this.containerData.get(0);
        return i != 0 ? i % 4 : 0;
    }
}

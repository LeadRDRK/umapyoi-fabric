package net.tracen.umapyoi.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tracen.umapyoi.container.TrainingFacilityContainer;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.UmaSoulItem;
import net.tracen.umapyoi.registry.training.SupportContainer;
import net.tracen.umapyoi.registry.umadata.Growth;
import net.tracen.umapyoi.utils.UmaSoulUtils;

public class TrainingFacilityBlockEntity extends SyncedInventoryEntity implements ExtendedScreenHandlerFactory {

    public static final int MAX_PROCESS_TIME = 260;
    private final NonNullList<ItemStack> items = NonNullList.withSize(7, ItemStack.EMPTY);

    protected final ContainerData tileData;

    private int recipeTime;

    public TrainingFacilityBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.TRAINING_FACILITY.get(), pos, state);
        this.tileData = createIntArray();
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot == 0) {
            if(!(stack.is(ItemRegistry.UMA_SOUL.get()) && UmaSoulUtils.getGrowth(stack) != Growth.RETIRED))
                return false;
            for (int i = 1; i < 7; i++) {
                ItemStack other = this.getItem(i);
                if (other.isEmpty())
                    continue;
                if (other.getItem()instanceof SupportContainer support) {
                    if (!(support.canSupport(TrainingFacilityBlockEntity.this.getLevel(), other).test(stack)))
                        return false;
                } else
                    return false;
            }
            return true;
        }
        else {
            if (stack.getItem() instanceof SupportContainer support) {
                var soul = this.getItem(0);
                for (int i = 1; i < 7; i++) {
                    ItemStack other = this.getItem(i);
                    if(!soul.isEmpty()) {
                        if (!support.canSupport(TrainingFacilityBlockEntity.this.getLevel(), stack).test(soul))
                            return false;
                    }

                    if (i == slot || other.isEmpty())
                        continue;

                    if (!(support.canSupport(TrainingFacilityBlockEntity.this.getLevel(), stack).test(other)))
                        return false;
                }
            } else {
                return false;
            }
            return true;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        if (slot != 0) {
            // support slots
            return 1;
        }
        return super.getSlotLimit(slot);
    }

    public static void workingTick(Level level, BlockPos pos, BlockState state,
            TrainingFacilityBlockEntity blockEntity) {
        if (level.isClientSide())
            return;

        boolean didInventoryChange = false;
        if (blockEntity.canWork()) {
            didInventoryChange = blockEntity.processRecipe();
        } else {
            blockEntity.recipeTime = 0;
        }

        if (didInventoryChange) {
            blockEntity.setChanged();
        }
    }

    private boolean processRecipe() {
        if (level == null) {
            return false;
        }

        ++recipeTime;
        if (recipeTime < MAX_PROCESS_TIME) {
            return false;
        }

        recipeTime = 0;

        ItemStack resultStack = getResultItem();
        setItem(0, resultStack);
        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1F, 1F);
        for (int i = 1; i < 7; i++) {
            ItemStack supportItem = getItem(i);
            if (supportItem.getItem() instanceof SupportContainer supports) {
                if (supports.isConsumable(this.getLevel(), supportItem))
                    supportItem.shrink(1);
                else
                    supportItem.hurtAndBreak(1, this.getLevel().getRandom(), null, () -> {
                        this.getLevel().playSound(null, this.getBlockPos(),
                                SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1F, 1F);
                    });
            }
        }
        
        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1F, 1F);
        return true;
    }

    private ItemStack getResultItem() {
        if (this.level == null)
            return ItemStack.EMPTY;
        ItemStack result = getItem(0).copy();
        UmaSoulUtils.setGrowth(result, Growth.TRAINED);
        UmaSoulUtils.downPhysique(result);
        for (int i = 1; i < 7; i++) {
            ItemStack supportItem = getItem(i);
            if (supportItem.getItem()instanceof SupportContainer supports) {
                supports.getSupports(this.getLevel(), supportItem).forEach(support -> support.applySupport(result, this.level.getRandom()));
            }
        }
        return result;
    }

    private boolean canWork() {
        if (!this.hasInput())
            return false;
        return true;
    }

    private boolean hasInput() {
        ItemStack input = getItem(0);
        if (input.getItem() instanceof UmaSoulItem) {
            if (UmaSoulUtils.getGrowth(input) != Growth.RETIRED && UmaSoulUtils.getPhysique(input) > 0) {
                for (int i = 1; i < 7; i++) {
                    ItemStack supportItem = getItem(i);
                    if (supportItem.getItem() instanceof SupportContainer supports) {
                        if (!(supports.canSupport(level, supportItem).test(input)))
                            return false;
                    }

                }
                return true;
            }
        }

        return false;
    }

    public NonNullList<ItemStack> getDroppableItems() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < 7; ++i) {
            drops.add(getItem(i));
        }
        return drops;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        clearContent();
        ContainerHelper.loadAllItems(compound, items, registries);
        recipeTime = compound.getInt("RecipeTime");
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("RecipeTime", recipeTime);
        ContainerHelper.saveAllItems(compound, items, registries);
    }

    private CompoundTag writeItems(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        ContainerHelper.saveAllItems(compound, items, registries);
        return compound;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return writeItems(new CompoundTag(), registries);
    }

    private ContainerData createIntArray() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                switch (index) {
                case 0:
                    return TrainingFacilityBlockEntity.this.recipeTime;
                default:
                    return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                case 0:
                    TrainingFacilityBlockEntity.this.recipeTime = value;
                    break;
                }
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
        return new TrainingFacilityContainer(id, player, this, this.tileData);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.umapyoi.training_facility");
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(getBlockPos());
    }
}

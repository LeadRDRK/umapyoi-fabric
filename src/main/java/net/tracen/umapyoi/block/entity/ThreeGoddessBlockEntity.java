package net.tracen.umapyoi.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.container.ThreeGoddessContainer;
import net.tracen.umapyoi.item.FadedUmaSoulItem;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.UmaDataRegistry;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.UmaFactorUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import java.util.List;

public class ThreeGoddessBlockEntity extends SyncedInventoryEntity implements MenuProvider {
    public static final int MAX_PROCESS_TIME = 200;
    private final NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    protected final ContainerData tileData;

    private int recipeTime;

    public int getProcessTime() {
        return recipeTime;
    }

    private int animationTime;
    public int getAnimationTime() {
        return animationTime;
    }

    public ThreeGoddessBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.THREE_GODDESS.get(), pos, state);
        this.tileData = createIntArray();
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public static void workingTick(Level level, BlockPos pos, BlockState state, ThreeGoddessBlockEntity blockEntity) {
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

    public static void animationTick(Level level, BlockPos pos, BlockState state, ThreeGoddessBlockEntity blockEntity) {
        blockEntity.animationTime++;
        if (blockEntity.canWork())
            ThreeGoddessBlockEntity.addWorkingParticle(level, pos);
        blockEntity.animationTime %= 360;
    }

    private static void addWorkingParticle(Level pLevel, BlockPos pPos) {
        RandomSource pRand = pLevel.getRandom();
        List<BlockPos> posOffsets = BlockPos.betweenClosedStream(-2, 0, -2, 2, 1, 2).filter(pos -> {
            return Math.abs(pos.getX()) == 2 || Math.abs(pos.getZ()) == 2;
        }).map(BlockPos::immutable).toList();
        for (BlockPos spawnPos : posOffsets) {
            if (pRand.nextInt(32) == 0) {
                pLevel.addParticle(ParticleTypes.ENCHANT, (double) pPos.getX() + 0.5D, (double) pPos.getY() + 2.5D,
                        (double) pPos.getZ() + 0.5D, (double) ((float) spawnPos.getX() + pRand.nextFloat() * 2F) - 0.5D,
                        (double) ((float) spawnPos.getY() + 2D - pRand.nextFloat()),
                        (double) ((float) spawnPos.getZ() + pRand.nextFloat() * 2F) - 0.5D);
            }
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

        setItem(3, resultStack.copy());

        getItem(0).shrink(1);

        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1F, 1F);
        return true;
    }

    private ItemStack getResultItem() {
        if (this.level == null)
            return ItemStack.EMPTY;

        ItemStack left = getItem(1);
        ItemStack right = getItem(2);
        Registry<UmaData> registry = UmapyoiAPI.getUmaDataRegistry(this.getLevel());

        ResourceLocation name = ResourceLocation
                .tryParse(getItem(0).getOrCreateTag().getString("name"));
        name = registry.containsKey(name) ? name : UmaDataRegistry.COMMON_UMA.getId();

        UmaData data = registry.getOptional(name).orElse(UmaDataRegistry.COMMON_UMA.get());

        ItemStack result = UmaSoulUtils.initUmaSoul(ItemRegistry.UMA_SOUL.get().getDefaultInstance(), name, data)
                .copy();

        if (!left.isEmpty() && !right.isEmpty()) {
            UmaFactorUtils.deserializeNBT(left.getOrCreateTag()).forEach(fac -> fac.applyFactor(result));
            UmaFactorUtils.deserializeNBT(right.getOrCreateTag()).forEach(fac -> fac.applyFactor(result));
        }

        return result;
    }

    private boolean canWork() {
        if (!this.hasInput())
            return false;
        ItemStack outputStack = getItem(3);
        if (outputStack.isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean hasInput() {
        if (getItem(0).getItem() instanceof FadedUmaSoulItem) {
            if (getItem(1).isEmpty() == getItem(2).isEmpty())
                return true;
        }
        return false;
    }

    public NonNullList<ItemStack> getDroppableItems() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < 4; ++i) {
            drops.add(getItem(i));
        }
        return drops;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        clearContent();
        ContainerHelper.loadAllItems(compound, items);
        recipeTime = compound.getInt("RecipeTime");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("RecipeTime", recipeTime);
        ContainerHelper.saveAllItems(compound, items);
    }

    private CompoundTag writeItems(CompoundTag compound) {
        super.saveAdditional(compound);
        ContainerHelper.saveAllItems(compound, items);
        return compound;
    }

    @Override
    public CompoundTag getUpdateTag() {
        return writeItems(new CompoundTag());
    }

    private ContainerData createIntArray() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0:
                        return ThreeGoddessBlockEntity.this.recipeTime;
                    default:
                        return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        ThreeGoddessBlockEntity.this.recipeTime = value;
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
        return new ThreeGoddessContainer(id, player, this, this.tileData);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.umapyoi.three_goddess");
    }
}

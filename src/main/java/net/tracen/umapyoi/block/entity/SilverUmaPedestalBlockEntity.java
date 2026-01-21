package net.tracen.umapyoi.block.entity;

import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.UmapyoiConfigModel;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.data.builtin.UmaDataRegistry;
import net.tracen.umapyoi.data.tag.UmapyoiItemTags;
import net.tracen.umapyoi.item.FadedUmaSoulItem;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.GachaRanking;
import net.tracen.umapyoi.utils.GachaUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SilverUmaPedestalBlockEntity extends AbstractPedestalBlockEntity implements Gachable {

    public static final int MAX_PROCESS_TIME = 200;
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    protected final ContainerData tileData;

    private int recipeTime;

    public int getProcessTime() {
        return recipeTime;
    }
    
    private int animationTime;
    public int getAnimationTime() {
        return animationTime;
    }

    public SilverUmaPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SILVER_UMA_PEDESTAL.get(), pos, state);
        this.tileData = createIntArray();
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public static void workingTick(Level level, BlockPos pos, BlockState state, SilverUmaPedestalBlockEntity blockEntity) {
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

    public static void animationTick(Level level, BlockPos pos, BlockState state, SilverUmaPedestalBlockEntity blockEntity) {
        if (blockEntity.canWork())
            ClientUtils.addSummonParticle(level, pos);
        if(!blockEntity.getStoredItem().isEmpty()) {
            blockEntity.animationTime++;
            blockEntity.animationTime %= 360;
        } else {
            blockEntity.animationTime = 0;  
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
        setItem(0, resultStack.copy());
        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1F, 1F);
        return true;
    }

    private ItemStack getResultItem() {
        if (this.level == null)
            return ItemStack.EMPTY;

        RandomSource rand = this.getLevel().getRandom();
        Registry<UmaData> registry = UmapyoiAPI.getUmaDataRegistry(this.getLevel());

        @NotNull
        Collection<ResourceLocation> keys = registry.keySet().stream()
                .filter(this.getFilter(getLevel(), getStoredItem()))
                .collect(Collectors.toCollection(Lists::newArrayList));

        ResourceLocation holder = keys.stream().skip(keys.isEmpty() ? 0 : rand.nextInt(keys.size())).findFirst()
                .orElse(UmaDataRegistry.COMMON_UMA.location());

        ItemStack result = FadedUmaSoulItem.genUmaSoul(holder, registry.get(holder));
        return result;
    }

    private boolean canWork() {
        ItemStack item = getStoredItem();
        return !item.isEmpty() && item.is(UmapyoiItemTags.UMA_TICKET) && !item.is(UmapyoiItemTags.SSR_UMA_TICKET);
    }

    public ItemStack getStoredItem() {
        return getItem(0);
    }

    public boolean isEmpty() {
        return getItem(0).isEmpty();
    }

    public boolean addItem(ItemStack itemStack) {
        if (isEmpty() && !itemStack.isEmpty()) {
            setItem(0, itemStack.split(1));
            setChanged();
            return true;
        }
        return false;
    }

    public ItemStack removeItem() {
        if (!isEmpty()) {
            ItemStack item = getStoredItem().split(1);
            setChanged();
            return item;
        }
        return ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        drops.add(getItem(0));
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
                    return SilverUmaPedestalBlockEntity.this.recipeTime;
                default:
                    return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                case 0:
                    SilverUmaPedestalBlockEntity.this.recipeTime = value;
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
    public Predicate<? super ResourceLocation> getFilter(Level level, ItemStack input) {
        return resloc -> {
            if (input.has(DataComponentsTypeRegistry.DATA_LOCATION.get())) {
                return resloc.equals(input.get(DataComponentsTypeRegistry.DATA_LOCATION.get()));
            }
            if (input.is(UmapyoiItemTags.SR_UMA_TICKET))
                return UmapyoiAPI.getUmaDataRegistry(level).get(resloc).ranking() == GachaRanking.SR;
            if (input.is(UmapyoiItemTags.COMMON_GACHA_ITEM))
                return UmapyoiAPI.getUmaDataRegistry(level).get(resloc).ranking() == GachaRanking.R;
            boolean cfgFlag = GachaUtils.checkGachaConfig();
            int gacha_roll;
            int ssrHit = cfgFlag ? Umapyoi.CONFIG.GACHA_PROBABILITY_SSR()
                    : UmapyoiConfigModel.DEFAULT_GACHA_PROBABILITY_SSR;
            gacha_roll = level.getRandom().nextInt(
                    cfgFlag ? Umapyoi.CONFIG.GACHA_PROBABILITY_SUM() : UmapyoiConfigModel.DEFAULT_GACHA_PROBABILITY_SUM);
            int srHit = ssrHit + (cfgFlag ? Umapyoi.CONFIG.GACHA_PROBABILITY_SR() : UmapyoiConfigModel.DEFAULT_GACHA_PROBABILITY_SR);
            return UmapyoiAPI.getUmaDataRegistry(level).get(resloc)
                    .ranking() == (gacha_roll < srHit ? GachaRanking.SR : GachaRanking.R);
        };
    }

}

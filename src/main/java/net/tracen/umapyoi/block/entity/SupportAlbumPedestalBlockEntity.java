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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.config.UmapyoiConfig;
import net.tracen.umapyoi.data.tag.UmapyoiItemTags;
import net.tracen.umapyoi.events.SupportCardGachaCallback;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.training.card.SupportCard;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.GachaRanking;
import net.tracen.umapyoi.utils.GachaUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SupportAlbumPedestalBlockEntity extends AbstractPedestalBlockEntity implements Gachable {

    public int time;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    public float rot;
    public float oRot;
    public float tRot;
    private static final Random RANDOM = new Random();

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

    public SupportAlbumPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SUPPORT_ALBUM_PEDESTAL.get(), pos, state);
        this.tileData = createIntArray();
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public static void workingTick(Level level, BlockPos pos, BlockState state,
            SupportAlbumPedestalBlockEntity blockEntity) {
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

    public static void animationTick(Level level, BlockPos pos, BlockState state,
            SupportAlbumPedestalBlockEntity blockEntity) {
        if (blockEntity.canWork())
            ClientUtils.addSummonParticle(level, pos);
        if(!blockEntity.getStoredItem().isEmpty()) {
            blockEntity.animationTime++;
            blockEntity.animationTime %= 360;
        } else {
            blockEntity.animationTime = 0;  
        }
        SupportAlbumPedestalBlockEntity.bookAnimationTick(level, pos, state, blockEntity);
    }

    private static void bookAnimationTick(Level pLevel, BlockPos pPos, BlockState pState,
            SupportAlbumPedestalBlockEntity pBlockEntity) {
        pBlockEntity.oOpen = pBlockEntity.open;
        pBlockEntity.oRot = pBlockEntity.rot;
        Player player = pLevel.getNearestPlayer((double) pPos.getX() + 0.5D, (double) pPos.getY() + 0.5D,
                (double) pPos.getZ() + 0.5D, 3.0D, false);
        if (player != null) {
            double d0 = player.getX() - ((double) pPos.getX() + 0.5D);
            double d1 = player.getZ() - ((double) pPos.getZ() + 0.5D);
            pBlockEntity.tRot = (float) Mth.atan2(d1, d0);
        } else {
            pBlockEntity.tRot += 0.02F;
        }

        if (!pBlockEntity.isEmpty()) {
            pBlockEntity.open += 0.1F;
            if (pBlockEntity.open < 0.5F || RANDOM.nextInt(40) == 0) {
                float f1 = pBlockEntity.flipT;

                do {
                    pBlockEntity.flipT += (float) (RANDOM.nextInt(4) - RANDOM.nextInt(4));
                } while (f1 == pBlockEntity.flipT);
            }
        } else {
            pBlockEntity.open -= 0.1F;
        }

        while (pBlockEntity.rot >= (float) Math.PI) {
            pBlockEntity.rot -= ((float) Math.PI * 2F);
        }

        while (pBlockEntity.rot < -(float) Math.PI) {
            pBlockEntity.rot += ((float) Math.PI * 2F);
        }

        while (pBlockEntity.tRot >= (float) Math.PI) {
            pBlockEntity.tRot -= ((float) Math.PI * 2F);
        }

        while (pBlockEntity.tRot < -(float) Math.PI) {
            pBlockEntity.tRot += ((float) Math.PI * 2F);
        }

        float f2;
        for (f2 = pBlockEntity.tRot - pBlockEntity.rot; f2 >= (float) Math.PI; f2 -= ((float) Math.PI * 2F))
            ;

        while (f2 < -(float) Math.PI) {
            f2 += ((float) Math.PI * 2F);
        }

        pBlockEntity.rot += f2 * 0.4F;
        pBlockEntity.open = Mth.clamp(pBlockEntity.open, 0.0F, 1.0F);
        ++pBlockEntity.time;
        pBlockEntity.oFlip = pBlockEntity.flip;
        float f = (pBlockEntity.flipT - pBlockEntity.flip) * 0.4F;
        float f3 = 0.2F;
        f = Mth.clamp(f, -0.2F, f3);
        pBlockEntity.flipA += (f - pBlockEntity.flipA) * 0.9F;
        pBlockEntity.flip += pBlockEntity.flipA;
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
        Registry<SupportCard> registry = UmapyoiAPI.getSupportCardRegistry(this.getLevel());

        RandomSource copyRand = rand.fork();

        @NotNull
        Collection<ResourceLocation> keys = registry.keySet().stream()
                .filter(this.getFilter(getLevel(), getStoredItem()))
                .collect(Collectors.toCollection(Lists::newArrayList));

        ResourceLocation key = keys.stream().skip(keys.isEmpty() ? 0 : rand.nextInt(keys.size())).findFirst()
                .orElse(ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "blank_card"));

        ItemStack result = SupportCard.init(key, registry.get(key));
        var evt = new SupportCardGachaCallback.Context(getStoredItem(), keys, key, result, copyRand);
        SupportCardGachaCallback.invoke(evt);
        return evt.getOutput();
    }

    private boolean canWork() {
        return !getStoredItem().isEmpty() && getStoredItem().is(UmapyoiItemTags.CARD_TICKET);
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

    public NonNullList<ItemStack> getDroppableItems() {
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
                    return SupportAlbumPedestalBlockEntity.this.recipeTime;
                default:
                    return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                case 0:
                    SupportAlbumPedestalBlockEntity.this.recipeTime = value;
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
            if (input.is(UmapyoiItemTags.SSR_CARD_TICKET))
                return UmapyoiAPI.getSupportCardRegistry(level).get(resloc).getGachaRanking() == GachaRanking.SSR;
            if (input.is(UmapyoiItemTags.COMMON_GACHA_ITEM))
                return UmapyoiAPI.getSupportCardRegistry(level).get(resloc).getGachaRanking() == GachaRanking.R;
            boolean cfgFlag = GachaUtils.checkGachaConfig();
            int gacha_roll;
            int ssrHit = cfgFlag ? Umapyoi.CONFIG.GACHA_PROBABILITY_SSR
                    : UmapyoiConfig.DEFAULT_GACHA_PROBABILITY_SSR;
            if (input.is(UmapyoiItemTags.SR_CARD_TICKET)) {
//              Set gacha roll, 30 = 100 - 70(default).  
                gacha_roll = level.getRandom()
                        .nextInt(cfgFlag
                                ? Umapyoi.CONFIG.GACHA_PROBABILITY_SUM - Umapyoi.CONFIG.GACHA_PROBABILITY_R
                                : 30);

                return UmapyoiAPI.getSupportCardRegistry(level).get(resloc)
                        .getGachaRanking() == (gacha_roll < ssrHit ? GachaRanking.SSR : GachaRanking.SR);
            }
            gacha_roll = level.getRandom().nextInt(
                    cfgFlag ? Umapyoi.CONFIG.GACHA_PROBABILITY_SUM : UmapyoiConfig.DEFAULT_GACHA_PROBABILITY_SUM);
            int srHit = ssrHit + (cfgFlag ? Umapyoi.CONFIG.GACHA_PROBABILITY_SR : UmapyoiConfig.DEFAULT_GACHA_PROBABILITY_SR);
            return UmapyoiAPI.getSupportCardRegistry(level).get(resloc)
                    .getGachaRanking() == (gacha_roll < ssrHit ? GachaRanking.SSR
                            : gacha_roll < srHit ? GachaRanking.SR : GachaRanking.R);
        };
    }

}

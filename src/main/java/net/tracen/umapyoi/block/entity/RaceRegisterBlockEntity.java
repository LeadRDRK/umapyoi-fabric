package net.tracen.umapyoi.block.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.container.RaceContainer;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.races.Race;
import net.tracen.umapyoi.utils.ItemHandlerHelper;
import net.tracen.umapyoi.utils.RaceRanking;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.IntStream;

import static net.tracen.umapyoi.item.UmaRaceTicketItem.getRaceID;

public class RaceRegisterBlockEntity extends SyncedInventoryEntity implements ExtendedScreenHandlerFactory, WorldlyContainer {
    // No additional synchronized logic because provided by SyncedBlockEntity
    // Update by inventoryChanged (custom logic in SyncedBlockEntity, which is the super of this class)

    // public static final int MAX_RECIPE_TIME = 260; //13 seconds;

    private final NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);;

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    private int recipeTime;
    private int maxRecipeTime = 0;
    protected final ContainerData tileData;

    public RaceRegisterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.RACE_REGISTER_BLOCK_ENTITY.get(), pos, state);
        this.tileData = createIntArray();
        this.maxRecipeTime = 0;
    }

    protected static boolean umaSoulConstraint(ItemStack stack) {
        return stack.is(ItemRegistry.UMA_SOUL.get());
    }

    protected static boolean raceConstraint(ItemStack stack) {
        return stack.is(ItemRegistry.UMA_RACE_TICKET.get());
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot >= 2) return false;
        if (!super.isItemValid(slot, stack)) return false;
        if (slot == 0) {
            return umaSoulConstraint(stack);
        } else {
            return raceConstraint(stack);
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        setChanged();
    }

    // NBT: {RecipeTime: this.recipeTime, Inventory: [...this.inventory]}

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);
        ContainerHelper.loadAllItems(compound, items);
        recipeTime = compound.getInt("RecipeTime");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("RecipeTime", recipeTime);
        ContainerHelper.saveAllItems(compound, items);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.UP ? new int[]{0, 1} : IntStream.range(2, 6).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        return direction == Direction.UP;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return direction != Direction.UP;
    }

    private ContainerData createIntArray() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch(index) {
                    case 0 -> RaceRegisterBlockEntity.this.recipeTime;
                    case 1 -> RaceRegisterBlockEntity.this.maxRecipeTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                if (index == 0) RaceRegisterBlockEntity.this.recipeTime = value;
                if (index == 1) RaceRegisterBlockEntity.this.maxRecipeTime = value;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
                                  RaceRegisterBlockEntity raceRegisterBlockEntity) {
        if (level.isClientSide())
            return;
        raceRegisterBlockEntity.serverTick();
    }

    public void serverTick() {
        boolean dirty = false;
        if (this.fulfill()) {
            dirty = this.processRecipe();
        } else {
            this.recipeTime = 0;
        }

        if (dirty) {
            this.setChanged();
        }
    }

    public ItemStack insertItemToSlot(int slot, @NotNull ItemStack stack)
    {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        ItemStack existing = this.getItem(slot);
        int limit = Math.min(this.getSlotLimit(slot), existing.getMaxStackSize());

        if (!existing.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) return stack;
            limit -= existing.getCount();
        }

        if (limit <= 0) return stack;
        boolean reachedLimit = stack.getCount() > limit;

        if (existing.isEmpty()) {
            this.setItem(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
        } else {
            existing.grow(reachedLimit ? limit : stack.getCount());
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    private boolean processRecipe() {
        if (level == null) {
            this.maxRecipeTime = 0;
            return false;
        }

        if (recipeTime == 0) {
            // other sanity check, no further process during non-empty output area
            int cnt = 0;
            for (int i = 2; i < 6; i++) {
                if (!this.getItem(i).isEmpty()) cnt++;
            }
            if (cnt == 4) {
                this.maxRecipeTime = 0;
                return false;
            }
        }

        ItemStack stack = this.getItem(1);
        if (stack == ItemStack.EMPTY) { // sanity check
            this.maxRecipeTime = 0;
            return false;
        }

        ResourceLocation raceID = getRaceID(this.getItem(1));
        Race race = UmapyoiAPI.getRaceRegistry(this.level).get(raceID);
        if (race == null) {
            this.maxRecipeTime = 0;
            return false;
        }

        this.maxRecipeTime = race.length(this.getItem(0)) * 3 / 20;

        recipeTime++;

        if (recipeTime < this.maxRecipeTime) return false;

        recipeTime = 0; // done logic

        stack.shrink(1);
        ItemStack resultStack = getResultItem(raceID);
        long maxSize = Math.round(resultStack.getCount() * race.getUmaFactorCorrection(this.getItem(0), this.level));
        ArrayList<ItemStack> listItems = new ArrayList<>();
        while (maxSize >= 0 && listItems.size() < 4) {
            int cnt = Math.toIntExact(Math.min(resultStack.getMaxStackSize(), maxSize));
            listItems.add(resultStack.copyWithCount(cnt));
            maxSize -= cnt;
        }

        Umapyoi.getLogger().info("Follow up");
        race.followUp(this.getItem(0), this.level);
        // todo: increase uma soul status here (generic)

        // this.inventory.setStackInSlot(3, resultStack);
        listItems.forEach((fillStack) -> {
            for (int i = 2; i < 6 && !fillStack.isEmpty(); i++) {
                fillStack = this.insertItemToSlot(i, fillStack);
            }
        });
        this.setChanged();
        return true;
    }

    public ItemStack getResultItem(ResourceLocation raceID) {
        if (this.level == null) return ItemStack.EMPTY;

        Race race = UmapyoiAPI.getRaceRegistry(this.level).get(raceID);
        if (race != null) {
            Umapyoi.getLogger().info("Run {} with following properties: Distance={}, Surface={}", raceID, race.distance(this.getItem(0)), race.surface(this.getItem(0)));
        }
        ResourceLocation lootSpecify = new ResourceLocation(raceID.getNamespace(), "race/id/" + raceID.getPath());
        LootDataManager manager = Objects.requireNonNull(this.level.getServer()).getLootData();
        LootTable table = manager.getLootTable(lootSpecify);
        if (table == LootTable.EMPTY) {
            Umapyoi.getLogger().info("There doesn't exist a loot table for {}, falling back to generic rank + field table", raceID);
            if (race == null) {
                Umapyoi.getLogger().error("No such race! {}", raceID);
                return ItemStack.EMPTY;
            }
            ItemStack stackSoul = this.getItem(0);
            RaceRanking rank = race.ranking;
            if (stackSoul.equals(ItemStack.EMPTY)) {
                Umapyoi.getLogger().error("Umasoul is no longer present.");
                table = manager.getLootTable(new ResourceLocation(Umapyoi.MODID, "race/generic/race_" +
                        rank.name().toLowerCase()));
            } else {
                ResourceLocation field = race.field(this.level, stackSoul).id();
                table = manager.getLootTable(new ResourceLocation(field.getNamespace(), "race/generic/field/race_"
                        + field.getPath() + "_" + rank.name().toLowerCase()));
                if (table == LootTable.EMPTY) {
                    Umapyoi.getLogger().info("There doesn't exist a loot table for {} {}, falling back to generic table", field, rank);
                    table = manager.getLootTable(new ResourceLocation(Umapyoi.MODID, "race/generic/race_" +
                            rank.name().toLowerCase()));
                }
            }
        }

        LootParams lootParams = new LootParams.Builder((ServerLevel) this.level)
                .create(LootContextParamSets.EMPTY);

        ObjectArrayList<ItemStack> returns = table.getRandomItems(lootParams);
        if (returns.isEmpty()) {
            Umapyoi.getLogger().error("Rolls Empty! {}", raceID);
            return ItemStack.EMPTY;
        }
        return returns.get(0);
    }

    public boolean fulfill() {
        ItemStack stackSoul = this.getItem(0);
        ItemStack stackRace = this.getItem(1);
        // sanity check
        if (!umaSoulConstraint(stackSoul)) return false;
        if (!raceConstraint(stackRace)) return false;
        if (this.level == null) return false;
        Race race = UmapyoiAPI.getRaceRegistry(this.level).get(getRaceID(stackRace));
        if (race == null) return false;
        return race.isAvailableToUmaSoul(stackSoul);
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        return this.items;
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return Component.translatable("container.umapyoi.race");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @Nonnull Inventory inventory, @Nonnull Player player) {
        return new RaceContainer(i, inventory, this, this.tileData);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(getBlockPos());
    }
}

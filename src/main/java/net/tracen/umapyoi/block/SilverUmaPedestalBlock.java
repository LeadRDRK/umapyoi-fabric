package net.tracen.umapyoi.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.tracen.umapyoi.block.entity.BlockEntityRegistry;
import net.tracen.umapyoi.block.entity.SilverUmaPedestalBlockEntity;

public class SilverUmaPedestalBlock extends AbstractPedestalBlock {
    public static final MapCodec<SilverUmaPedestalBlock> CODEC = simpleCodec(
            SilverUmaPedestalBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public SilverUmaPedestalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.SILVER_UMA_PEDESTAL.get().create(pos, state);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof SilverUmaPedestalBlockEntity blockEntity) {
                return interactBEWithoutItem(level, pos, player, blockEntity.isEmpty(), blockEntity.removeItem()
                );
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof SilverUmaPedestalBlockEntity blockEntity) {
                return interactBEWithItem(stack, level, pos, player, hand, blockEntity, true);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof SilverUmaPedestalBlockEntity blockEntity) {
            Containers.dropContents(level, pos, blockEntity.getDroppableInventory());
            Containers.updateNeighboursAfterDestroy(state, level, pos);
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> blockEntity) {
        if (level.isClientSide) {
            return createTickerHelper(blockEntity, BlockEntityRegistry.SILVER_UMA_PEDESTAL.get(),
                    SilverUmaPedestalBlockEntity::animationTick);
        }
        return createTickerHelper(blockEntity, BlockEntityRegistry.SILVER_UMA_PEDESTAL.get(),
                SilverUmaPedestalBlockEntity::workingTick);
    }

    @Override
    protected void transformOnBook(Level level, BlockPos pos) {
        level.destroyBlock(pos, false);
        level.setBlock(pos, BlockRegistry.SILVER_SUPPORT_ALBUM_PEDESTAL.defaultBlockState(), UPDATE_ALL);
    }
}

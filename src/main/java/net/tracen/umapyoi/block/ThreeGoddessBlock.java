package net.tracen.umapyoi.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.tracen.umapyoi.block.entity.BlockEntityRegistry;
import net.tracen.umapyoi.block.entity.ThreeGoddessBlockEntity;

import javax.annotation.Nullable;

public class ThreeGoddessBlock extends BaseEntityBlock {
    public static final MapCodec<ThreeGoddessBlock> CODEC = simpleCodec(ThreeGoddessBlock::new);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public ThreeGoddessBlock(Properties p) {
        super(p);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.above()).is(BlockRegistry.THREE_GODDESS_UPPER)
                || pLevel.getBlockState(pPos.above()).isAir();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        pLevel.setBlock(pPos.above(), BlockRegistry.THREE_GODDESS_UPPER.defaultBlockState(), UPDATE_ALL);
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.THREE_GODDESS.get().create(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        if (blockpos.getY() < level.getMaxY()
                && level.getBlockState(blockpos.above()).canBeReplaced(context)) {
            BlockState state = this.defaultBlockState().setValue(FACING,
                    context.getHorizontalDirection().getOpposite());
            return state;
        } else {
            return null;
        }
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            var menu = state.getMenuProvider(level, pos);
            if (menu != null) {
                player.openMenu(menu);
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof ThreeGoddessBlockEntity blockEntity) {
            Containers.dropContents(level, pos, blockEntity.getDroppableItems());
            Containers.updateNeighboursAfterDestroy(state, level, pos);
        }
        if (level.getBlockState(pos.above()).is(BlockRegistry.THREE_GODDESS_UPPER)) {
            level.removeBlock(pos.above(), false);
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> blockEntity) {
        if (level.isClientSide()) {
            return createTickerHelper(blockEntity, BlockEntityRegistry.THREE_GODDESS.get(),
                    ThreeGoddessBlockEntity::animationTick);
        }
        return createTickerHelper(blockEntity, BlockEntityRegistry.THREE_GODDESS.get(),
                ThreeGoddessBlockEntity::workingTick);
    }
}

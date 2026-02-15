package net.tracen.umapyoi.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class StatuesUpperBlock extends Block {

    private final Block bottomBlock;
    
    protected final VoxelShape shape;
    
    public StatuesUpperBlock(Block bottom, Properties p) {
        this(bottom, Shapes.block(), p);
    }
    
    public StatuesUpperBlock(Block bottom, VoxelShape shape, Properties p) {
        super(p);
        this.bottomBlock = bottom;
        this.shape = shape;
    }

    @Override
    public Item asItem() {
        return this.bottomBlock.asItem();
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    public Block getBottomBlock() {
        return bottomBlock;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.shape;
    }
    
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.below()).is(this.getBottomBlock());
    }

    @Override
    public void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        if (level.getBlockState(pos.below()).is(this.getBottomBlock())) {
            level.destroyBlock(pos.below(), true);
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
                                @Nullable Orientation orientation, boolean movedByPiston) {
        if (!level.isClientSide()) {
            if (!state.canSurvive(level, pos)) {
                level.removeBlock(pos, false);
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return level.getBlockState(pos.below()).useWithoutItem(level, player, hitResult.withPosition(pos.below()));
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return level.getBlockState(pos.below()).useItemOn(stack, level, player, hand, hitResult.withPosition(pos.below()));
    }
}

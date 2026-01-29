package net.tracen.umapyoi.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.tracen.umapyoi.utils.ClientUtils;

import java.util.Random;

public abstract class AbstractSupportAlbumPedestalBlockEntity extends AbstractPedestalBlockEntity {

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

    public AbstractSupportAlbumPedestalBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    public static void animationTick(Level level, BlockPos pos, BlockState state,
                                     AbstractSupportAlbumPedestalBlockEntity blockEntity) {
        AbstractPedestalBlockEntity.animationTick(level, pos, state, blockEntity);
        blockEntity.bookAnimationTick(level, pos, state);
    }

    private void bookAnimationTick(Level pLevel, BlockPos pPos, BlockState pState) {
        this.oOpen = this.open;
        this.oRot = this.rot;
        Player player = pLevel.getNearestPlayer((double) pPos.getX() + 0.5D, (double) pPos.getY() + 0.5D,
                (double) pPos.getZ() + 0.5D, 3.0D, false);
        if (player != null) {
            double d0 = player.getX() - ((double) pPos.getX() + 0.5D);
            double d1 = player.getZ() - ((double) pPos.getZ() + 0.5D);
            this.tRot = (float) Mth.atan2(d1, d0);
        } else {
            this.tRot += 0.02F;
        }

        if (!this.isEmpty()) {
            this.open += 0.1F;
            if (this.open < 0.5F || RANDOM.nextInt(40) == 0) {
                float f1 = this.flipT;

                do {
                    this.flipT += (float) (RANDOM.nextInt(4) - RANDOM.nextInt(4));
                } while (f1 == this.flipT);
            }
        } else {
            this.open -= 0.1F;
        }

        while (this.rot >= (float) Math.PI) {
            this.rot -= ((float) Math.PI * 2F);
        }

        while (this.rot < -(float) Math.PI) {
            this.rot += ((float) Math.PI * 2F);
        }

        while (this.tRot >= (float) Math.PI) {
            this.tRot -= ((float) Math.PI * 2F);
        }

        while (this.tRot < -(float) Math.PI) {
            this.tRot += ((float) Math.PI * 2F);
        }

        float f2;
        for (f2 = this.tRot - this.rot; f2 >= (float) Math.PI; f2 -= ((float) Math.PI * 2F))
            ;

        while (f2 < -(float) Math.PI) {
            f2 += ((float) Math.PI * 2F);
        }

        this.rot += f2 * 0.4F;
        this.open = Mth.clamp(this.open, 0.0F, 1.0F);
        ++this.time;
        this.oFlip = this.flip;
        float f = (this.flipT - this.flip) * 0.4F;
        float f3 = 0.2F;
        f = Mth.clamp(f, -0.2F, f3);
        this.flipA += (f - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }
}

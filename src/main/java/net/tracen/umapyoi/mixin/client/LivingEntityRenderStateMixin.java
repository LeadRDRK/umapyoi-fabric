package net.tracen.umapyoi.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.client.renderer.entity.state.UmapyoiRenderState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements UmapyoiRenderState {
    @Unique private ItemStack umaSoul = ItemStack.EMPTY;
    @Unique private int earTailAnimationOffset = -1;

    @Override
    public ItemStack umapyoi$getUmaSoul() {
        return umaSoul;
    }

    @Override
    public void umapyoi$setUmaSoul(ItemStack umaSoul) {
        this.umaSoul = umaSoul;
    }

    @Override
    public int umapyoi$getEarTailAnimationOffset() {
        return earTailAnimationOffset;
    }

    @Override
    public void umapyoi$setEarTailAnimationOffset(int earTailAnimationOffset) {
        this.earTailAnimationOffset = earTailAnimationOffset;
    }
}

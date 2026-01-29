package net.tracen.umapyoi.client.renderer.entity.state;

import net.minecraft.world.item.ItemStack;

public interface UmapyoiRenderState {
    default ItemStack umapyoi$getUmaSoul() {
        return ItemStack.EMPTY;
    }

    default void umapyoi$setUmaSoul(ItemStack umaSoul) {
    }

    default int umapyoi$getEarTailAnimationOffset() {
        return 0;
    }

    default void umapyoi$setEarTailAnimationOffset(int earTailAnimationOffset) {
    }
}

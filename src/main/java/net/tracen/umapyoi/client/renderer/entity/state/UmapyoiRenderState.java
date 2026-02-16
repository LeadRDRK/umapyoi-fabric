package net.tracen.umapyoi.client.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.client.model.UmaPlayerModel;

import org.jetbrains.annotations.Nullable;

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

    @Nullable
    default UmaPlayerModel<HumanoidRenderState> umapyoi$getUmaModel() {
        return null;
    }

    default void umapyoi$setUmaModel(UmaPlayerModel<HumanoidRenderState> umaModel) {
    }

    @Nullable
    default UmaPlayerModel<HumanoidRenderState> umapyoi$getSuitModel() {
        return null;
    }

    default void umapyoi$setSuitModel(UmaPlayerModel<HumanoidRenderState> suitModel) {
    }

    default ResourceLocation umapyoi$getUmaTexture() {
        return null;
    }

    default void umapyoi$setUmaTexture(ResourceLocation umaTexture) {
    }

    default ResourceLocation umapyoi$getSuitTexture() {
        return null;
    }

    default void umapyoi$setSuitTexture(ResourceLocation suitTexture) {
    }

    default ResourceLocation umapyoi$getUmaEmissiveTexture() {
        return null;
    }

    default void umapyoi$setUmaEmissiveTexture(ResourceLocation umaEmissiveTexture) {
    }
}

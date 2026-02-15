package net.tracen.umapyoi.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.tracen.umapyoi.block.entity.AbstractPedestalBlockEntity;
import net.tracen.umapyoi.client.renderer.blockentity.state.PedestalBlockRenderState;

import org.jetbrains.annotations.Nullable;

public abstract class AbstractPedestalBlockRenderer<T extends AbstractPedestalBlockEntity, S extends PedestalBlockRenderState> implements BlockEntityRenderer<T, S> {
    @Override
    public void submit(S renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector,
                       CameraRenderState cameraRenderState) {
        renderAnimation(renderState, poseStack, nodeCollector);
    }

    protected void renderAnimation(S renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector) {
        ItemStack item = renderState.item;
        if (item.isEmpty())
            return;

        BlockPos pPos = renderState.blockPos;
        this.renderItem(renderState, poseStack);

        var itemStackRenderState = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForTopItem(itemStackRenderState, item,
                ItemDisplayContext.FIXED, null, null, (int) pPos.asLong());
        itemStackRenderState.submit(poseStack, nodeCollector, renderState.lightCoords,
                OverlayTexture.NO_OVERLAY, 0);
    }

    private void renderItem(S renderState, PoseStack poseStack) {
        float f = renderState.itemTime;
        float f1 = Mth.sin(f) * 0.1F + 0.1F;
        poseStack.translate(0.5D, f1 + 1.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotation(f));
        poseStack.scale(0.6F, 0.6F, 0.6F);
    }

    @Override
    public void extractRenderState(T blockEntity, S renderState,
                                   float partialTick, Vec3 cameraPosition,
                                   @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.item = blockEntity.getStoredItem();
        renderState.itemTime = (blockEntity.getAnimationTime() + partialTick) / 20.0F;
    }
}

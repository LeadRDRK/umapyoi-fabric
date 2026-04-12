package net.tracen.umapyoi.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.block.ThreeGoddessBlock;
import net.tracen.umapyoi.block.entity.ThreeGoddessBlockEntity;
import net.tracen.umapyoi.client.model.SimpleBedrockModel;
import net.tracen.umapyoi.client.renderer.BedrockModelRenderer;
import net.tracen.umapyoi.client.renderer.blockentity.state.ThreeGoddessBlockRenderState;
import net.tracen.umapyoi.utils.ClientUtils;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class ThreeGoddessBlockRenderer implements BlockEntityRenderer<ThreeGoddessBlockEntity, ThreeGoddessBlockRenderState> {
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
            Umapyoi.MODID, "textures/model/three_goddesses.png");
    private final SimpleBedrockModel model;

    public ThreeGoddessBlockRenderer(BlockEntityRendererProvider.Context context) {
        model = new SimpleBedrockModel();
    }

    @Override
    public void submit(ThreeGoddessBlockRenderState renderState, PoseStack poseStack,
                       SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        renderModel(renderState, renderState.direction, poseStack, nodeCollector);
        renderAnimation(renderState, poseStack, nodeCollector);
    }

    private void renderModel(ThreeGoddessBlockRenderState renderState, Direction direction,
                             PoseStack poseStack, SubmitNodeCollector nodeCollector) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 1.5D, 0.5D);

        poseStack.mulPose(new Quaternionf().rotateY(ClientUtils.convertRotation(-direction.toYRot())));
        poseStack.mulPose(new Quaternionf().rotateX(ClientUtils.convertRotation(180)));

        var pojo = ClientUtils.getModelPOJO(ClientUtils.THREE_GODDESS);
        if (model.needRefresh(pojo))
            model.loadModel(pojo);
        var modelRenderer = new BedrockModelRenderer(model, renderState.lightCoords,
                OverlayTexture.NO_OVERLAY, -1);
        nodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(TEXTURE), modelRenderer);
        poseStack.popPose();
    }

    private void renderAnimation(ThreeGoddessBlockRenderState renderState, PoseStack poseStack,
                                 SubmitNodeCollector nodeCollector) {
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

    private void renderItem(ThreeGoddessBlockRenderState renderState, PoseStack poseStack) {
        float f = renderState.time;
        float f1 = Mth.sin(f) * 0.1F + 0.1F;
        poseStack.translate(0.5D, f1 + 3.0D, 0.5D);
        poseStack.mulPose(new Quaternionf().rotateY(f));
        poseStack.scale(0.6F, 0.6F, 0.6F);
    }

    @Override
    public ThreeGoddessBlockRenderState createRenderState() {
        return new ThreeGoddessBlockRenderState();
    }

    @Override
    public void extractRenderState(ThreeGoddessBlockEntity blockEntity,
                                   ThreeGoddessBlockRenderState renderState,
                                   float partialTick, Vec3 cameraPosition,
                                   @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.direction = blockEntity.getBlockState().getValue(ThreeGoddessBlock.FACING);
        renderState.time = (blockEntity.getAnimationTime() + partialTick) / 20.0F;
        ItemStack jewel = blockEntity.getItem(0);
        ItemStack soul = blockEntity.getItem(3);
        renderState.item = soul.isEmpty() ? jewel : soul;
    }
}

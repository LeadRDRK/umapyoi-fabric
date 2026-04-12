package net.tracen.umapyoi.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.block.GateDoor;
import net.tracen.umapyoi.block.ThreeGoddessBlock;
import net.tracen.umapyoi.block.entity.GateEntity;
import net.tracen.umapyoi.client.model.pojo.BedrockModelPOJO;
import net.tracen.umapyoi.client.renderer.BedrockModelRenderer;
import net.tracen.umapyoi.client.renderer.blockentity.state.GateRenderState;
import net.tracen.umapyoi.utils.ClientUtils;

import org.jetbrains.annotations.Nullable;

public class GateRender implements BlockEntityRenderer<GateEntity, GateRenderState> {
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/gate_door.png");

    public GateRender(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public boolean shouldRender(GateEntity pBlockEntity, Vec3 pCameraPos) {
        Level world = pBlockEntity.getLevel();
        if (world == null) return false;
        return BlockEntityRenderer.super.shouldRender(pBlockEntity, pCameraPos);
    }

    @Override
    public void submit(GateRenderState renderState, PoseStack poseStack,
                       SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0.5d, 1.5d + 0.25d, 0.5d);
        poseStack.mulPose(Axis.YN.rotationDegrees(renderState.direction.toYRot() + 180));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        // poseStack.translate(0d, 0d, 7d/16d);
        var modelRenderer = new BedrockModelRenderer(renderState.model, renderState.lightCoords,
                OverlayTexture.NO_OVERLAY, -1);
        var renderType = RenderTypes.entityCutout(TEXTURE);
        nodeCollector.submitCustomGeometry(poseStack, renderType, modelRenderer);
        poseStack.popPose();
    }

    @Override
    public GateRenderState createRenderState() {
        return new GateRenderState();
    }

    @Override
    public void extractRenderState(GateEntity blockEntity, GateRenderState renderState,
                                   float partialTick, Vec3 cameraPosition,
                                   @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);

        renderState.direction = blockEntity.getBlockState().getValue(ThreeGoddessBlock.FACING);

        BlockState state = blockEntity.getBlockState();
        boolean isOpen;
        try {
            isOpen = state.getValue(GateDoor.OPEN);
        } catch (IllegalArgumentException ignore) {
            isOpen = false;
        }
        float tuneTick = isOpen ? partialTick : -partialTick;
        float renderProgress = Mth.clamp(((float) blockEntity.open) + tuneTick, 0f, (float) GateEntity.MAX_OPEN) / (float) GateEntity.MAX_OPEN;
        double angle = Math.toRadians(Mth.rotLerp(renderProgress, 15f, 90f));

        var model = renderState.model;
        BedrockModelPOJO pojo = ClientUtils.getModelPOJO(Umapyoi.id("gate_door"));
        if (model.needRefresh(pojo)) model.loadModel(pojo);
        model.getChild("door_left").yRot = (float) -angle;
        model.getChild("door_right").yRot = (float) angle;
    }
}

package net.tracen.umapyoi.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.block.UmaStatueBlock;
import net.tracen.umapyoi.block.entity.UmaStatueBlockEntity;
import net.tracen.umapyoi.client.model.UmaPlayerModel;
import net.tracen.umapyoi.client.renderer.BedrockModelRenderer;
import net.tracen.umapyoi.client.renderer.blockentity.state.UmaStatueBlockRenderState;
import net.tracen.umapyoi.item.AbstractSuitItem;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import org.jetbrains.annotations.Nullable;

public class UmaStatueBlockRenderer implements BlockEntityRenderer<UmaStatueBlockEntity, UmaStatueBlockRenderState> {
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/three_goddesses.png");

    public UmaStatueBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void submit(UmaStatueBlockRenderState renderState, PoseStack poseStack,
                       SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        Direction direction = renderState.blockState.getValue(UmaStatueBlock.FACING);
        renderModel(renderState, direction, poseStack, nodeCollector);
    }

    private void renderModel(UmaStatueBlockRenderState renderState, Direction direction,
                             PoseStack poseStack, SubmitNodeCollector nodeCollector) {

        poseStack.pushPose();
        poseStack.translate(0.5D, 1.5D, 0.5D);

        poseStack.mulPose(Axis.YN.rotationDegrees(direction.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        var model = renderState.model;
        var modelRenderer = new BedrockModelRenderer(model, renderState.lightCoords,
                OverlayTexture.NO_OVERLAY, -1);
        var renderType = RenderTypes.entityTranslucent(renderState.texture);
        nodeCollector.submitCustomGeometry(poseStack, renderType, modelRenderer);

        if (model.isEmissive()) {
            var emissiveRenderer = new BedrockModelRenderer(model, renderState.lightCoords,
                    OverlayTexture.NO_OVERLAY, -1, true);
            var emissiveRenderType = RenderTypes.entityTranslucentEmissive(renderState.emissiveTexture);
            nodeCollector.order(1)
                    .submitCustomGeometry(poseStack, emissiveRenderType, emissiveRenderer);
        }

        var suitModel = renderState.suitModel;
        if (suitModel != null) {
            var suitRenderer = new BedrockModelRenderer(suitModel, renderState.lightCoords,
                    OverlayTexture.NO_OVERLAY, -1);
            var suitRenderType = RenderTypes.entityTranslucent(renderState.suitTexture);
            nodeCollector.submitCustomGeometry(poseStack, suitRenderType, suitRenderer);
        }

        poseStack.popPose();
    }

    @Override
    public UmaStatueBlockRenderState createRenderState() {
        return new UmaStatueBlockRenderState();
    }

    @Override
    public void extractRenderState(UmaStatueBlockEntity blockEntity,
                                   UmaStatueBlockRenderState renderState,
                                   float partialTick, Vec3 cameraPosition,
                                   @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);

        var soul = blockEntity.getStoredItem();
        var suit = blockEntity.getCostume();
        var useDefaultModel = !soul.is(ItemRegistry.UMA_SOUL);

        var umaId = UmaSoulUtils.getName(soul);
        var pojo = useDefaultModel
                ? ClientUtils.getModelPOJO(ClientUtils.UMA_STATUES)
                : ClientUtils.getModelPOJO(umaId);
        var model = renderState.model;
        if (model.needRefresh(pojo)) {
            model.loadModel(pojo);

            model.leftArm.zRot = ClientUtils.convertRotation(-5);
            model.rightArm.zRot = ClientUtils.convertRotation(5);

            if (useDefaultModel) {
                renderState.texture = TEXTURE;
                renderState.emissiveTexture = TEXTURE;
            }
            else {
                renderState.texture = ClientUtils.getTexture(umaId);
                renderState.emissiveTexture = renderState.model.isEmissive()
                        ? ClientUtils.getEmissiveTexture(umaId)
                        : null;

                if (renderState.suitModel != null) {
                    ClientUtils.setUmaModelVisibilityForSuit(model, suit, renderState.suitModel);
                }
            }
        }

        // no need to check for suit if soul isn't present
        if (useDefaultModel) return;

        var doRenderSuit = false;
        if (suit.getItem() instanceof AbstractSuitItem suitItem) {
            boolean isFlat = ClientUtils.isFlatUmamusume(soul);
            var suitPojo = ClientUtils.getModelPOJO(isFlat
                    ? suitItem.getFlatModel(suit)
                    : suitItem.getModel(suit));
            if (suitPojo != null) {
                if (renderState.suitModel == null)
                    renderState.suitModel = new UmaPlayerModel<>();
                var suitModel = renderState.suitModel;
                doRenderSuit = true;

                if (suitModel.needRefresh(suitPojo)) {
                    suitModel.loadModel(suitPojo);

                    suitModel.leftArm.zRot = model.leftArm.zRot;
                    suitModel.rightArm.zRot = model.rightArm.zRot;
                    suitModel.head.visible = false;
                    suitModel.tail.visible = false;

                    boolean isTanned = ClientUtils.isTannedSkin(soul);
                    renderState.suitTexture = isFlat
                            ? suitItem.getFlatTexture(suit, isTanned)
                            : suitItem.getTexture(suit, isTanned);

                    ClientUtils.setUmaModelVisibilityForSuit(model, suit, renderState.suitModel);
                }
            }
        }

        if (!doRenderSuit && renderState.suitModel != null) {
            renderState.suitModel = null;
            renderState.suitTexture = null;
            model.setAllVisible(true);
        }
    }
}

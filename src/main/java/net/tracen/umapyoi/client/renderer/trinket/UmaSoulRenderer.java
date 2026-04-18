package net.tracen.umapyoi.client.renderer.trinket;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.client.renderer.BedrockModelRenderer;
import net.tracen.umapyoi.compat.FPMCompat;
import net.tracen.umapyoi.events.client.RenderingUmaSoulCallback;
import net.tracen.umapyoi.item.ItemRegistry;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.client.TrinketRenderer;
import eu.pb4.trinkets.api.client.TrinketRendererRegistry;

public class UmaSoulRenderer implements TrinketRenderer {
    public static UmaSoulRenderer INSTANCE = new UmaSoulRenderer();

    @Override
    @Environment(EnvType.CLIENT)
    public void submit(
            ItemStack itemStack, TrinketSlotAccess slotAccess, EntityModel<? extends LivingEntityRenderState> entityModel,
            PoseStack poseStack, SubmitNodeCollector nodeCollector, int light, LivingEntityRenderState entityState,
            float limbAngle, float limbDistance
    ) {
        // match AvatarRenderState directly (disallow ArmorStandRenderState)
        if (!(entityState instanceof AvatarRenderState state) || (state.isInvisible && !state.isSpectator))
            return;

        var baseModel = state.umapyoi$getUmaModel();
        if (baseModel == null) return;

        var renderType = RenderTypes.entityTranslucent(state.umapyoi$getUmaTexture());
        baseModel.setModelProperties(state);
        baseModel.prepareMobModel(state, limbAngle, limbDistance);

        var callbackContext = new RenderingUmaSoulCallback.Context(slotAccess, state, baseModel,
                poseStack, nodeCollector, light);
        if (RenderingUmaSoulCallback.Pre.invoke(callbackContext))
            return;
        FPMCompat.hideHeadIfRendering(state, baseModel);

        if (entityModel instanceof HumanoidModel<?> humanoidModel) {
            baseModel.copyAnim(baseModel.head, humanoidModel.head);
            baseModel.copyAnim(baseModel.body, humanoidModel.body);
            baseModel.copyAnim(baseModel.leftArm, humanoidModel.leftArm);
            baseModel.copyAnim(baseModel.leftLeg, humanoidModel.leftLeg);
            baseModel.copyAnim(baseModel.rightArm, humanoidModel.rightArm);
            baseModel.copyAnim(baseModel.rightLeg, humanoidModel.rightLeg);
        }
        baseModel.setupAnim(state);
        var modelRenderer = new BedrockModelRenderer(baseModel, light,
                LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1);
        nodeCollector.submitCustomGeometry(poseStack, renderType, modelRenderer);
        if (baseModel.isEmissive()) {
            var emissiveRenderType = RenderTypes.entityTranslucentEmissive(state.umapyoi$getUmaEmissiveTexture());
            var emissiveRenderer = new BedrockModelRenderer(baseModel, light,
                    LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1, true);
            nodeCollector.order(1)
                    .submitCustomGeometry(poseStack, emissiveRenderType, emissiveRenderer);
        }

        RenderingUmaSoulCallback.Post.invoke(callbackContext);
    }

    public static void register() {
        TrinketRendererRegistry.registerRenderer(ItemRegistry.UMA_SOUL, INSTANCE);
    }
}

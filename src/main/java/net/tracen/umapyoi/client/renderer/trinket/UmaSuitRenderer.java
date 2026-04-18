package net.tracen.umapyoi.client.renderer.trinket;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.client.renderer.BedrockModelRenderer;
import net.tracen.umapyoi.compat.FPMCompat;
import net.tracen.umapyoi.events.client.RenderingUmaSuitCallback;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.client.TrinketRenderer;
import eu.pb4.trinkets.api.client.TrinketRendererRegistry;

public class UmaSuitRenderer implements TrinketRenderer {
    public static UmaSuitRenderer INSTANCE = new UmaSuitRenderer();

    @Override
    @Environment(EnvType.CLIENT)
    public void submit(
            ItemStack itemStack, TrinketSlotAccess slotAccess, EntityModel<? extends LivingEntityRenderState> entityModel,
            PoseStack poseStack, SubmitNodeCollector nodeCollector, int light, LivingEntityRenderState entityState,
            float limbAngle, float limbDistance
    ) {
        if (!(entityState instanceof HumanoidRenderState state) || state.isInvisible)
            return;

        var baseModel = state.umapyoi$getSuitModel();
        if (baseModel == null) return;

        var renderType = RenderTypes.entityTranslucent(state.umapyoi$getSuitTexture());
        baseModel.setModelProperties(state);
        baseModel.head.visible = false;
        baseModel.tail.visible = false;
        baseModel.prepareMobModel(state, limbAngle, limbDistance);
        var callbackContext = new RenderingUmaSuitCallback.Context(slotAccess, state, baseModel,
                poseStack, nodeCollector, light);
        if (RenderingUmaSuitCallback.Pre.invoke(callbackContext))
            return;
        FPMCompat.hideHeadIfRendering(state, baseModel);

        if (entityModel instanceof HumanoidModel) {
            @SuppressWarnings("unchecked")
            var model = (HumanoidModel<HumanoidRenderState>)entityModel;

            baseModel.copyAnim(baseModel.head, model.head);
            baseModel.copyAnim(baseModel.body, model.body);
            baseModel.copyAnim(baseModel.leftArm, model.leftArm);
            baseModel.copyAnim(baseModel.leftLeg, model.leftLeg);
            baseModel.copyAnim(baseModel.rightArm, model.rightArm);
            baseModel.copyAnim(baseModel.rightLeg, model.rightLeg);
        }
        baseModel.setupAnim(state);

        var modelRenderer = new BedrockModelRenderer(baseModel, light,
                LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1);
        nodeCollector.submitCustomGeometry(poseStack, renderType, modelRenderer);
        RenderingUmaSuitCallback.Post.invoke(callbackContext);
    }

    public static void register(Item item) {
        TrinketRendererRegistry.registerRenderer(item, INSTANCE);
    }
}

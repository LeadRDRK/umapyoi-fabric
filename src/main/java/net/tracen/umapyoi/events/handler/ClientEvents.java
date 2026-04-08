package net.tracen.umapyoi.events.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.client.model.UmaCostumeModelUtils;
import net.tracen.umapyoi.client.model.UmaPlayerModel;
import net.tracen.umapyoi.client.model.bedrock.BedrockPart;
import net.tracen.umapyoi.client.model.pojo.BedrockModelPOJO;
import net.tracen.umapyoi.client.renderer.BedrockPartRenderer;
import net.tracen.umapyoi.events.client.RenderArmCallback;
import net.tracen.umapyoi.events.client.RenderingModelCallback;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

@Environment(EnvType.CLIENT)
public class ClientEvents {
    public static boolean preUmaSoulRendering(RenderingModelCallback.Context event) {
        LivingEntity entity = event.getWearer();
        var model = event.getModel();

        var suitModel = event.getRenderState().umapyoi$getSuitModel();
        if (suitModel != null && UmapyoiAPI.isUmaSuitRendering(entity)) {
            var suitItem = UmapyoiAPI.getUmaSuit(entity);
            ClientUtils.setUmaModelVisibilityForSuit(model, suitItem, suitModel);
        }

        // continue
        return false;
    }

    private static final UmaPlayerModel<HumanoidRenderState> baseModel = new UmaPlayerModel<>();

    private static void renderArmModel(RenderArmCallback.Context event, ResourceLocation name,
                                       RenderType renderType, BedrockModelPOJO pojo) {
        if (baseModel.needRefresh(pojo))
            baseModel.loadModel(pojo);

        //baseModel.setModelProperties(event.getState());
        //baseModel.attackTime = 0.0F;
        baseModel.crouching = false;
        baseModel.swimAmount = 0.0F;
        //baseModel.setupAnim(event.getState(), 0.0F, 0.0F);

        BedrockPart armPart;
        float xOffset;
        if (event.getArm() == HumanoidArm.RIGHT) {
            armPart = baseModel.rightArm;
            xOffset = -1F;
        } else {
            armPart = baseModel.leftArm;
            xOffset = 1F;
        }

        var nodeCollector = event.getNodeCollector();
        armPart.xRot = 0.0F;
        armPart.x += xOffset;
        var partRenderer = new BedrockPartRenderer(armPart, event.getPackedLight(),
                OverlayTexture.NO_OVERLAY, -1);
        nodeCollector.submitCustomGeometry(event.getPoseStack(), renderType, partRenderer);
        if (baseModel.isEmissive()) {
            var emissiveRenderType = RenderType.entityTranslucentEmissive(ClientUtils.getEmissiveTexture(name));
            var emissiveRenderer = new BedrockPartRenderer(armPart, event.getPackedLight(),
                    OverlayTexture.NO_OVERLAY, -1, true);
            nodeCollector.order(1)
                    .submitCustomGeometry(event.getPoseStack(), emissiveRenderType, emissiveRenderer);
        }
        armPart.x -= xOffset;
    }

    public static boolean onPlayerArmRendering(RenderArmCallback.Context event) {
        Player player = event.getPlayer();
        ItemStack umasoul = UmapyoiAPI.getRenderingUmaSoul(player);
        ItemStack umasuit = UmapyoiAPI.getUmaSuit(player);
        if (!umasoul.isEmpty()) {
            ResourceLocation name = UmaSoulUtils.getName(umasoul);
            BedrockModelPOJO pojo;
            RenderType renderType;
            if (umasuit.isEmpty()) {
                renderType = RenderType.entityTranslucent(ClientUtils.getTexture(name));
                pojo = ClientUtils.getModelPOJO(name);
            }
            else {
                boolean tanned = ClientUtils.isTannedSkin(umasoul);
                renderType = RenderType.entityTranslucent(UmaCostumeModelUtils.getCostumeTexture(umasuit, tanned));
                pojo = ClientUtils.getModelPOJO(UmaCostumeModelUtils.getCostumeModel(umasuit));
            }
            renderArmModel(event, name, renderType, pojo);
            return true;
        }
        return false;
    }
}

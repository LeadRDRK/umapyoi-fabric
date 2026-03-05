package net.tracen.umapyoi.events.handler;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
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
import net.tracen.umapyoi.events.client.RenderArmCallback;
import net.tracen.umapyoi.events.client.RenderingModelCallback;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

@Environment(EnvType.CLIENT)
public class ClientEvents {
    public static boolean preUmaSoulRendering(RenderingModelCallback.Context event) {
        LivingEntity entity = event.getWearer();
        var model = event.getModel();

        if (UmapyoiAPI.isUmaSuitRendering(entity)) {
            var suitItem = UmapyoiAPI.getUmaSuit(entity);
            ClientUtils.setUmaModelVisibilityForSuit(model, suitItem);
        }

        // continue
        return false;
    }

    private static final UmaPlayerModel<LivingEntity> baseModel = new UmaPlayerModel<>();

    private static void renderArmModel(RenderArmCallback.Context event, ResourceLocation name, VertexConsumer vertexconsumer,
                                       BedrockModelPOJO pojo) {
        if(baseModel.needRefresh(pojo))
            baseModel.loadModel(pojo);

        baseModel.setModelProperties(event.getPlayer());
        baseModel.attackTime = 0.0F;
        baseModel.crouching = false;
        baseModel.swimAmount = 0.0F;
        baseModel.setupAnim(event.getPlayer(), 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

        BedrockPart armPart;
        float xOffset;
        if (event.getArm() == HumanoidArm.RIGHT) {
            armPart = baseModel.rightArm;
            xOffset = -1F;
        } else {
            armPart = baseModel.leftArm;
            xOffset = 1F;
        }

        armPart.xRot = 0.0F;
        armPart.x += xOffset;
        armPart.render(event.getPoseStack(), vertexconsumer, event.getPackedLight(),
                OverlayTexture.NO_OVERLAY);
        if (baseModel.isEmissive()) {
            VertexConsumer emissiveConsumer = event.getMultiBufferSource()
                    .getBuffer(RenderType.entityTranslucentEmissive(ClientUtils.getEmissiveTexture(name)));
            armPart.renderEmissive(event.getPoseStack(), emissiveConsumer, event.getPackedLight(),
                    OverlayTexture.NO_OVERLAY, -1);
        }
        armPart.x -= xOffset;
    }

    public static boolean onPlayerArmRendering(RenderArmCallback.Context event) {
        Player player = event.getPlayer();
        ItemStack umaSoul = UmapyoiAPI.getRenderingUmaSoul(player);
        ItemStack umaSuit = UmapyoiAPI.getUmaSuit(player);
        if (!umaSoul.isEmpty()) {
            ResourceLocation name = UmaSoulUtils.getName(umaSoul);
            VertexConsumer vertexConsumer;
            BedrockModelPOJO pojo;
            if (umaSuit.isEmpty()) {
                vertexConsumer = event.getMultiBufferSource()
                        .getBuffer(RenderType.entityTranslucent(ClientUtils.getTexture(name)));
                pojo = ClientUtils.getModelPOJO(name);
            }
            else {
                boolean tanned = ClientUtils.isTannedSkin(umaSoul);
                vertexConsumer = event.getMultiBufferSource()
                        .getBuffer(RenderType.entityTranslucent(UmaCostumeModelUtils.getCostumeTexture(umaSuit, tanned)));
                pojo = ClientUtils.getModelPOJO(UmaCostumeModelUtils.getCostumeModel(umaSuit));
            }

            if (pojo == null)
                return false;

            renderArmModel(event, name, vertexConsumer, pojo);
            return true;
        }
        return false;
    }
}

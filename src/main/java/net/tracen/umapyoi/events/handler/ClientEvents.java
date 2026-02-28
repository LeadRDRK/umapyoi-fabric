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

        if (event.getArm() == HumanoidArm.RIGHT) {
            baseModel.rightArm.xRot = 0.0F;
            baseModel.rightArm.x -=1F;
            baseModel.rightArm.render(event.getPoseStack(), vertexconsumer, event.getPackedLight(),
                    OverlayTexture.NO_OVERLAY);
            if(baseModel.isEmissive()) {
                VertexConsumer emissiveConsumer = event.getMultiBufferSource()
                        .getBuffer(RenderType.entityTranslucentEmissive(ClientUtils.getEmissiveTexture(name)));
                baseModel.rightArm.renderEmissive(event.getPoseStack(), emissiveConsumer, event.getPackedLight(),
                        OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
            baseModel.rightArm.x +=1F;
        } else {
            baseModel.leftArm.xRot = 0.0F;
            baseModel.leftArm.x +=1F;
            baseModel.leftArm.render(event.getPoseStack(), vertexconsumer, event.getPackedLight(),
                    OverlayTexture.NO_OVERLAY);
            if(baseModel.isEmissive()) {
                VertexConsumer emissiveConsumer = event.getMultiBufferSource()
                        .getBuffer(RenderType.entityTranslucentEmissive(ClientUtils.getEmissiveTexture(name)));
                baseModel.leftArm.renderEmissive(event.getPoseStack(), emissiveConsumer, event.getPackedLight(),
                        OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
            baseModel.leftArm.x -=1F;
        }
    }

    public static boolean onPlayerArmRendering(RenderArmCallback.Context event) {
        Player player = event.getPlayer();
        ItemStack umasoul = UmapyoiAPI.getRenderingUmaSoul(player);
        ItemStack umasuit = UmapyoiAPI.getUmaSuit(player);
        if (!umasoul.isEmpty()) {
            ResourceLocation name = UmaSoulUtils.getName(umasoul);
            VertexConsumer vertexconsumer = event.getMultiBufferSource()
                    .getBuffer(RenderType.entityTranslucent(ClientUtils.getTexture(name)));
            var pojo = ClientUtils.getModelPOJO(name);
            if(!umasuit.isEmpty()) {
                boolean tanned = ClientUtils.isTannedSkin(umasoul);
                vertexconsumer = event.getMultiBufferSource()
                        .getBuffer(RenderType.entityTranslucent(UmaCostumeModelUtils.getCostumeTexture(umasuit, tanned)));
                pojo = ClientUtils.getModelPOJO(UmaCostumeModelUtils.getCostumeModel(umasuit));
            }
            renderArmModel(event, name, vertexconsumer, pojo);
            return true;
        }
        return false;
    }
}

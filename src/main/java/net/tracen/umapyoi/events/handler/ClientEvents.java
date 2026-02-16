package net.tracen.umapyoi.events.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.client.model.UmaCostumeModelUtils;
import net.tracen.umapyoi.client.model.UmaPlayerModel;
import net.tracen.umapyoi.client.model.bedrock.BedrockPart;
import net.tracen.umapyoi.client.model.pojo.BedrockModelPOJO;
import net.tracen.umapyoi.client.renderer.BedrockPartRenderer;
import net.tracen.umapyoi.data.tag.UmapyoiCostumeDataTags;
import net.tracen.umapyoi.events.client.RenderArmCallback;
import net.tracen.umapyoi.events.client.RenderingModelCallback;
import net.tracen.umapyoi.item.UmaCostumeItem;
import net.tracen.umapyoi.registry.cosmetics.CosmeticData;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

@Environment(EnvType.CLIENT)
public class ClientEvents {
    public static boolean preUmaSoulRendering(RenderingModelCallback.Context event) {
        LivingEntity entity = event.getWearer();
        var model = event.getModel();
        boolean hideHair = false;
        if (UmapyoiAPI.isUmaSuitRendering(entity)) {
            model.setAllVisible(false);
            model.head.visible = true;
            model.tail.visible = true;
            if(UmapyoiAPI.isUmaSuitHasHat(entity)) {
                Identifier loc = UmaCostumeItem.getCostumeID(UmapyoiAPI.getUmaSuit(entity));
                var costumeData = ClientUtils.getClientCosmeticDataRegistry().get(
                        ResourceKey.create(CosmeticData.REGISTRY_KEY, loc)
                );
                if(costumeData.get().is(UmapyoiCostumeDataTags.HAT_HIDEHAIR)) {
                    hideHair = true;
                    model.longHairParts.forEach(part -> part.visible = false);
                }else {
                    model.longHairParts.forEach(part -> part.visible = true);
                }
                model.hideHat();
            }
            else {
                model.showHat();

            }
        } else {
            model.setAllVisible(true);
        }
        if(hideHair) {
            model.longHairParts.forEach(part -> part.visible = false);
        }else {
            model.longHairParts.forEach(part -> part.visible = true);
        }
        model.showEars();

        // continue
        return false;
    }

    private static final UmaPlayerModel<HumanoidRenderState> baseModel = new UmaPlayerModel<>();

    public static boolean onPlayerArmRendering(RenderArmCallback.Context event) {
        var player = event.getPlayer();
        ItemStack umasoul = UmapyoiAPI.getRenderingUmaSoul(player);
        ItemStack umasuit = UmapyoiAPI.getUmaSuit(player);
        if (!umasoul.isEmpty()) {
            Identifier name = UmaSoulUtils.getName(umasoul);
            BedrockModelPOJO pojo;
            RenderType renderType;
            if (umasuit.isEmpty()) {
                renderType = RenderTypes.entityTranslucent(getTexture(name));
                pojo = ClientUtils.getModelPOJO(name);
            }
            else {
                boolean tanned = ClientUtils.isTannedSkin(umasoul);
                renderType = RenderTypes.entityTranslucent(UmaCostumeModelUtils.getCostumeTexture(umasuit, tanned));
                pojo = ClientUtils.getModelPOJO(UmaCostumeModelUtils.getCostumeModel(umasuit));
            }
            renderArmModel(event, name, renderType, pojo);
            return true;
        }
        return false;
    }

    private static void renderArmModel(RenderArmCallback.Context event, Identifier name,
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
            var emissiveRenderType = RenderTypes.entityTranslucentEmissive(ClientUtils.getEmissiveTexture(name));
            var emissiveRenderer = new BedrockPartRenderer(armPart, event.getPackedLight(),
                    OverlayTexture.NO_OVERLAY, -1, true);
            nodeCollector.order(1)
                    .submitCustomGeometry(event.getPoseStack(), emissiveRenderType, emissiveRenderer);
        }
        armPart.x -= xOffset;
    }

    private static Identifier getTexture(Identifier name) {
        return Identifier.fromNamespaceAndPath(name.getNamespace(), "textures/model/" + name.getPath() + ".png");
    }
}

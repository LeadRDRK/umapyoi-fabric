package net.tracen.umapyoi.client.renderer.blockentity;

import static net.tracen.umapyoi.item.UmaSoulItem.getSuitTarget;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.block.UmaStatueBlock;
import net.tracen.umapyoi.block.entity.UmaStatueBlockEntity;
import net.tracen.umapyoi.client.model.UmaPlayerModel;
import net.tracen.umapyoi.client.model.pojo.BedrockModelPOJO;
import net.tracen.umapyoi.data.tag.UmapyoiUmaDataTags;
import net.tracen.umapyoi.item.AbstractSuitItem;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

public class UmaStatuesBlockRender implements BlockEntityRenderer<UmaStatueBlockEntity> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/three_goddesses.png");
    private final UmaPlayerModel<?> model;
    private final UmaPlayerModel<?> costumeModel;

    public UmaStatuesBlockRender(BlockEntityRendererProvider.Context context) {
        model = new UmaPlayerModel<>();
        costumeModel = new UmaPlayerModel<>();
    }

    @Override
    public void render(UmaStatueBlockEntity tileEntity, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level world = tileEntity.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? tileEntity.getBlockState()
                : BlockRegistry.UMA_STATUES.get().defaultBlockState();
        if (blockstate.getBlock() instanceof UmaStatueBlock) {
            Direction direction = tileEntity.getBlockState().getValue(UmaStatueBlock.FACING);
            renderModel(tileEntity, direction, poseStack, buffer, combinedLight, combinedOverlay);
        }
    }

    private static ResourceLocation getRenderTarget(UmaStatueBlockEntity tileEntity) {
        ItemStack item = tileEntity.getStoredItem();
        if (!tileEntity.isCostumeEmpty() && tileEntity.getCostume().getItem() instanceof AbstractSuitItem) {
            return getSuitTarget(item, ClientUtils.getClientUmaDataRegistry()
                    .getHolder(ResourceKey.create(UmaData.REGISTRY_KEY, UmaSoulUtils.getName(item)))
                    .get().is(UmapyoiUmaDataTags.ALTER_MODEL));
        }
        return UmaSoulUtils.getName(item);
    }

    private void renderModel(UmaStatueBlockEntity tileEntity, Direction direction, PoseStack poseStack,
                             MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemStack item = tileEntity.getStoredItem();
        BedrockModelPOJO pojo;
        if (item.isEmpty() || !item.is(ItemRegistry.UMA_SOUL.get())) {
            pojo = ClientUtils.getModelPOJO(ClientUtils.UMA_STATUES);
        }
        else {
            ResourceLocation target = getRenderTarget(tileEntity);
            pojo = ClientUtils.getModelPOJO(target);
            if (pojo == null)
                pojo = ClientUtils.getModelPOJO(ClientUtils.UMA_STATUES);
        }

        // will be null during resource reload
        if (pojo == null)
            return;

        if (model.needRefresh(pojo))
            model.loadModel(pojo);

        model.leftArm.zRot = ClientUtils.convertRotation(-5);
        model.rightArm.zRot = ClientUtils.convertRotation(5);

        boolean doRenderSuit = false;

        ResourceLocation costumeResource = null;

        ItemStack costumeItem = tileEntity.getCostume();
        if (item.is(ItemRegistry.UMA_SOUL.get()) && !tileEntity.isCostumeEmpty() && !tileEntity.isEmpty()) {
            if (costumeItem.getItem() instanceof AbstractSuitItem renderer) {
                boolean is_flat_chest = ClientUtils.isFlatUmamusume(item);
                boolean is_tanned = ClientUtils.isTannedSkin(item);

                var suitPojo = ClientUtils.getModelPOJO(is_flat_chest
                        ? renderer.getFlatModel(costumeItem)
                        : renderer.getModel(costumeItem));
                if (suitPojo != null) {
                    if (costumeModel.needRefresh(suitPojo))
                        costumeModel.loadModel(suitPojo);

                    costumeModel.leftArm.zRot = model.leftArm.zRot;
                    costumeModel.rightArm.zRot = model.rightArm.zRot;
                    costumeModel.head.visible = false;
                    costumeModel.tail.visible = false;

                    costumeResource = is_flat_chest ? renderer.getFlatTexture(costumeItem, is_tanned) : renderer.getTexture(costumeItem, is_tanned);
                    doRenderSuit = true;
                }
            }
        }

        if (doRenderSuit) {
            ClientUtils.setUmaModelVisibilityForSuit(model, costumeItem, costumeModel);
        }
        else {
            model.setAllVisible(true);
        }

        poseStack.pushPose();
        poseStack.translate(0.5D, 1.5D, 0.5D);

        poseStack.mulPose(Axis.YN.rotationDegrees(direction.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        VertexConsumer vertexConsumer = buffer
                .getBuffer(RenderType.entityTranslucent(tileEntity.isEmpty() ? TEXTURE : ClientUtils.getTexture(UmaSoulUtils.getName(item))));
        model.renderToBuffer(poseStack, vertexConsumer, combinedLight, combinedOverlay, -1);

        if(model.isEmissive()) {
            VertexConsumer emissiveConsumer = buffer
                    .getBuffer(RenderType.entityTranslucentEmissive(tileEntity.isEmpty() ? TEXTURE : ClientUtils.getEmissiveTexture(UmaSoulUtils.getName(item))));
            model.renderEmissiveParts(poseStack, emissiveConsumer, combinedLight, combinedOverlay, -1);
        }

        if (doRenderSuit) {
            VertexConsumer vertexConsumerSuit = buffer.getBuffer(RenderType.entityTranslucentCull(costumeResource));
            costumeModel.renderToBuffer(poseStack, vertexConsumerSuit, combinedLight, combinedOverlay, -1);
        }

        poseStack.popPose();
    }

}

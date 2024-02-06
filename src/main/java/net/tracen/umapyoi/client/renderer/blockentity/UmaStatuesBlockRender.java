package net.tracen.umapyoi.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.block.UmaStatueBlock;
import net.tracen.umapyoi.block.entity.UmaStatueBlockEntity;
import net.tracen.umapyoi.client.EmissiveRenderType;
import net.tracen.umapyoi.client.model.SimpleBedrockModel;
import net.tracen.umapyoi.client.model.bedrock.BedrockPart;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import org.joml.Quaternionf;

public class UmaStatuesBlockRender implements BlockEntityRenderer<UmaStatueBlockEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Umapyoi.MODID, "textures/model/three_goddesses.png");
    private final SimpleBedrockModel model;

    public UmaStatuesBlockRender(BlockEntityRendererProvider.Context context) {
        model = new SimpleBedrockModel();
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

    private void renderModel(UmaStatueBlockEntity tileEntity, Direction direction, PoseStack poseStack,
                             MultiBufferSource buffer, int combinedLight, int combinedOverlay) {

        poseStack.pushPose();
        poseStack.translate(0.5D, 1.5D, 0.5D);

        poseStack.mulPose(new Quaternionf().rotateY(ClientUtils.convertRotation(-direction.toYRot())));
        poseStack.mulPose(new Quaternionf().rotateX(ClientUtils.convertRotation(180)));
        ItemStack item = tileEntity.getStoredItem();
        var pojo = tileEntity.isEmpty() ? ClientUtils.getModelPOJO(ClientUtils.UMA_STATUES) : ClientUtils.getModelPOJO(UmaSoulUtils.getName(item));

        if (model.needRefresh(pojo))
            model.loadModel(pojo);

        var leftArm = model.getChild("left_arm") != null ? model.getChild("left_arm") : new BedrockPart();
        var rightArm = model.getChild("right_arm") != null ? model.getChild("right_arm") : new BedrockPart();
        leftArm.zRot = ClientUtils.convertRotation(-5);
        rightArm.zRot = ClientUtils.convertRotation(5);
        
        VertexConsumer vertexConsumer = buffer
                .getBuffer(RenderType.entityTranslucent(tileEntity.isEmpty() ? TEXTURE : ClientUtils.getTexture(UmaSoulUtils.getName(item))));
        model.renderToBuffer(poseStack, vertexConsumer, combinedLight, combinedOverlay, 1, 1, 1, 1);

        if (model.isEmissive()) {
            VertexConsumer emissiveConsumer = buffer
                    .getBuffer(EmissiveRenderType.emissive(tileEntity.isEmpty() ? TEXTURE : ClientUtils.getEmissiveTexture(UmaSoulUtils.getName(item))));
            model.renderEmissiveParts(poseStack, emissiveConsumer, combinedLight, combinedOverlay, 1, 1, 1, 1);
        }
        
        poseStack.popPose();
    }

}

package net.tracen.umapyoi.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.block.entity.AbstractSupportAlbumPedestalBlockEntity;
import net.tracen.umapyoi.client.renderer.blockentity.state.SupportAlbumPedestalBlockRenderState;

import org.jetbrains.annotations.Nullable;

public class SupportAlbumPedestalBlockRenderer extends AbstractPedestalBlockRenderer<AbstractSupportAlbumPedestalBlockEntity, SupportAlbumPedestalBlockRenderState> {
    public static final Material BOOK_LOCATION = Sheets.BLOCK_ENTITIES_MAPPER.apply(
            ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "support_card_album"));
    private final MaterialSet materials;
    private final BookModel bookModel;

    public SupportAlbumPedestalBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.materials = context.materials();
        this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
    }

    @Override
    public void submit(SupportAlbumPedestalBlockRenderState renderState, PoseStack poseStack,
                       SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        renderBook(renderState, poseStack, nodeCollector);
        renderAnimation(renderState, poseStack, nodeCollector);
    }

    public void renderBook(SupportAlbumPedestalBlockRenderState renderState, PoseStack poseStack,
                           SubmitNodeCollector nodeCollector) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 1.0F, 0.5F);
        poseStack.translate(0.0F, 0.1F + Mth.sin(renderState.bookTime * 0.1F) * 0.01F, 0.0F);
        float f = renderState.yRot;
        poseStack.mulPose(Axis.YP.rotation(-f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(80.0F));
        float g = Mth.frac(renderState.flip + 0.25F) * 1.6F - 0.3F;
        float h = Mth.frac(renderState.flip + 0.75F) * 1.6F - 0.3F;
        BookModel.State state = new BookModel.State(renderState.bookTime, Mth.clamp(g, 0.0F, 1.0F), Mth.clamp(h, 0.0F, 1.0F), renderState.open);
        nodeCollector.submitModel(
                this.bookModel,
                state,
                poseStack,
                BOOK_LOCATION.renderType(RenderType::entitySolid),
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1,
                this.materials.get(BOOK_LOCATION),
                0,
                renderState.breakProgress
        );
        poseStack.popPose();
    }

    @Override
    public SupportAlbumPedestalBlockRenderState createRenderState() {
        return new SupportAlbumPedestalBlockRenderState();
    }

    @Override
    public void extractRenderState(AbstractSupportAlbumPedestalBlockEntity blockEntity,
                                   SupportAlbumPedestalBlockRenderState renderState,
                                   float partialTick, Vec3 cameraPosition,
                                   @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.flip = Mth.lerp(partialTick, blockEntity.oFlip, blockEntity.flip);
        renderState.open = Mth.lerp(partialTick, blockEntity.oOpen, blockEntity.open);
        renderState.bookTime = blockEntity.time + partialTick;
        float g = blockEntity.rot - blockEntity.oRot;

        while (g >= (float) Math.PI) {
            g -= (float) (Math.PI * 2);
        }

        while (g < (float) -Math.PI) {
            g += (float) (Math.PI * 2);
        }

        renderState.yRot = blockEntity.oRot + g * partialTick;

        renderState.item = blockEntity.getStoredItem();
        renderState.itemTime = (blockEntity.getAnimationTime() + partialTick) / 20.0F;
    }
}

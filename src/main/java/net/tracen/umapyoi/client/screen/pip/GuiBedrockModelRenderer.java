package net.tracen.umapyoi.client.screen.pip;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.fabric.api.client.rendering.v1.SpecialGuiElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.client.model.SimpleBedrockModel;
import net.tracen.umapyoi.utils.ClientUtils;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class GuiBedrockModelRenderer extends PictureInPictureRenderer<GuiBedrockModelRenderer.RenderState> {
    public GuiBedrockModelRenderer(SpecialGuiElementRegistry.Context context) {
        super(context.vertexConsumers());
    }

    @Override
    public Class<RenderState> getRenderStateClass() {
        return RenderState.class;
    }

    @Override
    protected void renderToTexture(RenderState renderState, PoseStack poseStack) {
        Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
        Vector3f translation = renderState.translation;
        poseStack.translate(translation.x, translation.y, translation.z);
        poseStack.mulPose(renderState.rotation());
        VertexConsumer vertexconsumer = bufferSource
                .getBuffer(RenderType.entityTranslucent(ClientUtils.getTexture(renderState.texture())));
        renderState.model().renderToBuffer(poseStack, vertexconsumer, LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY, -1);
    }

    @Override
    protected String getTextureLabel() {
        return "bedrock model";
    }

    public record RenderState(
            SimpleBedrockModel model,
            ResourceLocation texture,
            Vector3f translation,
            Quaternionf rotation,
            int x0, int y0, int x1, int y1,
            float scale,
            @Nullable ScreenRectangle scissorArea,
            @Nullable ScreenRectangle bounds
    ) implements PictureInPictureRenderState {
        public RenderState(
                SimpleBedrockModel model,
                ResourceLocation texture,
                Vector3f translation,
                Quaternionf rotation,
                int x0, int y0, int x1, int y1,
                float scale,
                @Nullable ScreenRectangle scissorArea
        ) {
            this(model, texture, translation, rotation, x0, y0, x1, y1, scale, scissorArea,
                    PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
        }
    }
}

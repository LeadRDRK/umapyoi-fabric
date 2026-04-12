package net.tracen.umapyoi.client.screen.pip;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.fabric.api.client.rendering.v1.PictureInPictureRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.tracen.umapyoi.client.model.SimpleBedrockModel;
import net.tracen.umapyoi.utils.ClientUtils;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class GuiBedrockModelRenderer extends PictureInPictureRenderer<GuiBedrockModelRenderer.RenderState> {
    public GuiBedrockModelRenderer(PictureInPictureRendererRegistry.Context context) {
        super(context.bufferSource());
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
                .getBuffer(RenderTypes.entityTranslucent(ClientUtils.getTexture(renderState.texture())));
        renderState.model().renderToBuffer(poseStack, vertexconsumer, LightCoordsUtil.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY, -1);
    }

    @Override
    protected String getTextureLabel() {
        return "bedrock model";
    }

    public record RenderState(
            SimpleBedrockModel model,
            Identifier texture,
            Vector3f translation,
            Quaternionf rotation,
            int x0, int y0, int x1, int y1,
            float scale,
            @Nullable ScreenRectangle scissorArea,
            @Nullable ScreenRectangle bounds
    ) implements PictureInPictureRenderState {
        public RenderState(
                SimpleBedrockModel model,
                Identifier texture,
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

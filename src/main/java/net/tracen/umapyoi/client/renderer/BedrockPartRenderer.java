package net.tracen.umapyoi.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.tracen.umapyoi.client.model.bedrock.BedrockPart;

public class BedrockPartRenderer implements SubmitNodeCollector.CustomGeometryRenderer {
    private final BedrockPart part;
    private final int packedLight;
    private final int packedOverlay;
    private final int color;
    private final boolean renderEmissive;

    public BedrockPartRenderer(BedrockPart part, int packedLight, int packedOverlay, int color, boolean renderEmissive) {
        this.part = part;
        this.packedLight = packedLight;
        this.packedOverlay = packedOverlay;
        this.color = color;
        this.renderEmissive = renderEmissive;
    }

    public BedrockPartRenderer(BedrockPart part, int packedLight, int packedOverlay, int color) {
        this(part, packedLight, packedOverlay, color, false);
    }

    @Override
    public void render(PoseStack.Pose pose, VertexConsumer vertexConsumer) {
        var poseStack = new PoseStack();
        poseStack.last().set(pose);

        if (renderEmissive) {
            part.renderEmissive(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }
        else {
            part.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }
    }
}

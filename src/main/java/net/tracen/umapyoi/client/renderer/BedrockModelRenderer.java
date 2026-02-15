package net.tracen.umapyoi.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.tracen.umapyoi.client.model.bedrock.BedrockModel;

public class BedrockModelRenderer implements SubmitNodeCollector.CustomGeometryRenderer {
    private final BedrockModel model;
    private final int packedLight;
    private final int packedOverlay;
    private final int color;
    private final boolean renderEmissive;

    public BedrockModelRenderer(BedrockModel model, int packedLight, int packedOverlay, int color, boolean renderEmissive) {
        this.model = model;
        this.packedLight = packedLight;
        this.packedOverlay = packedOverlay;
        this.color = color;
        this.renderEmissive = renderEmissive;
    }

    public BedrockModelRenderer(BedrockModel model, int packedLight, int packedOverlay, int color) {
        this(model, packedLight, packedOverlay, color, false);
    }

    @Override
    public void render(PoseStack.Pose pose, VertexConsumer vertexConsumer) {
        var poseStack = new PoseStack();
        poseStack.last().set(pose);

        if (renderEmissive) {
            model.renderEmissiveParts(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }
        else {
            model.renderBedrockModel(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }
    }
}

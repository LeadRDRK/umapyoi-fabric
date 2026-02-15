package net.tracen.umapyoi.events.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.tracen.umapyoi.client.model.UmaPlayerModel;

@Environment(EnvType.CLIENT)
public interface RenderingModelCallback {
    class Context {
        private final LivingEntity entity;
        private final HumanoidRenderState state;
        private final UmaPlayerModel<HumanoidRenderState> model;
        private final PoseStack poseStack;
        private final SubmitNodeCollector nodeCollector;
        private final int packedLight;

        public Context(LivingEntity entity, HumanoidRenderState state, UmaPlayerModel<HumanoidRenderState> model, PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight) {
            this.entity = entity;
            this.state = state;
            this.model = model;
            this.poseStack = poseStack;
            this.nodeCollector = nodeCollector;
            this.packedLight = packedLight;
        }

        public LivingEntity getWearer() {
            return entity;
        }

        public HumanoidRenderState getRenderState() {
            return state;
        }

        public UmaPlayerModel<HumanoidRenderState> getModel() {
            return model;
        }

        public PoseStack getPoseStack() {
            return poseStack;
        }

        public SubmitNodeCollector getNodeCollector() {
            return nodeCollector;
        }

        public int getPackedLight() {
            return packedLight;
        }
    }

    boolean callback(Context context);
}

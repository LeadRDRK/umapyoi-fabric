package net.tracen.umapyoi.events.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.tracen.umapyoi.client.model.UmaPlayerModel;

@Environment(EnvType.CLIENT)
public interface RenderingModelCallback {
    class Context {
        private final LivingEntity entity;
        private final HumanoidRenderState state;
        private final UmaPlayerModel<HumanoidRenderState> model;
        private final PoseStack poseStack;
        private final MultiBufferSource multiBufferSource;
        private final int packedLight;

        public Context(LivingEntity entity, HumanoidRenderState state, UmaPlayerModel<HumanoidRenderState> model, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
            this.entity = entity;
            this.state = state;
            this.model = model;
            this.poseStack = poseStack;
            this.multiBufferSource = multiBufferSource;
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

        public MultiBufferSource getMultiBufferSource() {
            return multiBufferSource;
        }

        public int getPackedLight() {
            return packedLight;
        }
    }

    boolean callback(Context context);
}

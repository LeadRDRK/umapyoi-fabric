package net.tracen.umapyoi.events.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;

/**
 * Replacement for Forge's RenderArmEvent
 */
@Environment(EnvType.CLIENT)
public interface RenderArmCallback {
    class Context {
        private final AbstractClientPlayer player;
        private final PlayerRenderState state;
        private final PoseStack poseStack;
        private final MultiBufferSource bufferSource;
        private final int packedLight;
        private final ResourceLocation skinTexture;
        private final boolean isSleeveVisible;
        private final HumanoidArm arm;

        public Context(AbstractClientPlayer player, PlayerRenderState state, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, ResourceLocation skinTexture,
                       boolean isSleeveVisible, HumanoidArm arm) {
            this.player = player;
            this.state = state;
            this.poseStack = poseStack;
            this.bufferSource = bufferSource;
            this.packedLight = packedLight;
            this.skinTexture = skinTexture;
            this.isSleeveVisible = isSleeveVisible;
            this.arm = arm;
        }

        public AbstractClientPlayer getPlayer() {
            return player;
        }

        public PlayerRenderState getState() {
            return state;
        }

        public PoseStack getPoseStack() {
            return poseStack;
        }

        public MultiBufferSource getBufferSource() {
            return bufferSource;
        }

        public int getPackedLight() {
            return packedLight;
        }

        public ResourceLocation getSkinTexture() {
            return skinTexture;
        }

        public boolean isSleeveVisible() {
            return isSleeveVisible;
        }

        public HumanoidArm getArm() {
            return arm;
        }
    }
    boolean callback(Context context);

    Event<RenderArmCallback> EVENT = EventFactory.createArrayBacked(RenderArmCallback.class,
            (listeners) -> (context) -> {
                for (RenderArmCallback listener : listeners) {
                    if (listener.callback(context)) return true;
                }

                return false;
            });

    static boolean invoke(Context context) {
        return EVENT.invoker().callback(context);
    }
}

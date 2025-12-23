package net.tracen.umapyoi.events.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;

/**
 * Replacement for Forge's RenderArmEvent
 */
@Environment(EnvType.CLIENT)
public interface RenderArmCallback {
    class Context {
        private final PoseStack poseStack;
        private final MultiBufferSource multiBufferSource;
        private final int packedLight;
        private final AbstractClientPlayer player;
        private final HumanoidArm arm;

        public Context(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, AbstractClientPlayer player, HumanoidArm arm) {
            this.poseStack = poseStack;
            this.multiBufferSource = multiBufferSource;
            this.packedLight = packedLight;
            this.player = player;
            this.arm = arm;
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

        public AbstractClientPlayer getPlayer() {
            return player;
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

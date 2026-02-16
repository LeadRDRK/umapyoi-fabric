package net.tracen.umapyoi.events.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;

/**
 * Replacement for Forge's RenderArmEvent
 */
@Environment(EnvType.CLIENT)
public interface RenderArmCallback {
    class Context {
        private final AbstractClientPlayer player;
        private final PoseStack poseStack;
        private final SubmitNodeCollector nodeCollector;
        private final int packedLight;
        private final Identifier skinTexture;
        private final boolean renderSleeve;
        private final HumanoidArm arm;

        public Context(AbstractClientPlayer player, PoseStack poseStack, SubmitNodeCollector nodeCollector,
                       int packedLight, Identifier skinTexture, boolean renderSleeve, HumanoidArm arm) {
            this.player = player;
            this.poseStack = poseStack;
            this.nodeCollector = nodeCollector;
            this.packedLight = packedLight;
            this.skinTexture = skinTexture;
            this.renderSleeve = renderSleeve;
            this.arm = arm;
        }

        public AbstractClientPlayer getPlayer() {
            return player;
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

        public Identifier getSkinTexture() {
            return skinTexture;
        }

        public boolean isRenderSleeve() {
            return renderSleeve;
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

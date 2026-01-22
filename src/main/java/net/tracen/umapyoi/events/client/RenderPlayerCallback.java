package net.tracen.umapyoi.events.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;

/**
 * Replacement for Forge's RenderPlayerEvent
 */
@Environment(EnvType.CLIENT)
public interface RenderPlayerCallback {
    class Context {
        private final PlayerRenderer renderer;
        private final AbstractClientPlayer player;
        private final PlayerRenderState state;
        private final PoseStack poseStack;
        private final MultiBufferSource bufferSource;
        private final int packedLight;

        public Context(PlayerRenderer renderer, AbstractClientPlayer player, PlayerRenderState state,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
            this.renderer = renderer;
            this.player = player;
            this.state = state;
            this.poseStack = poseStack;
            this.bufferSource = bufferSource;
            this.packedLight = packedLight;
        }

        public PlayerRenderer getRenderer() {
            return renderer;
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
    }

    void callback(Context context);

    interface Pre extends RenderPlayerCallback {
        Event<Pre> EVENT = EventFactory.createArrayBacked(Pre.class,
                (listeners) -> (context) -> {
                    for (Pre listener : listeners) {
                        listener.callback(context);
                    }
                });

        static void invoke(Context context) {
            EVENT.invoker().callback(context);
        }
    }

    interface Post extends RenderPlayerCallback {
        Event<Post> EVENT = EventFactory.createArrayBacked(Post.class,
                (listeners) -> (context) -> {
                    for (Post listener : listeners) {
                        listener.callback(context);
                    }
                });

        static void invoke(Context context) {
            EVENT.invoker().callback(context);
        }
    }
}

package net.tracen.umapyoi.events.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

/**
 * Replacement for Forge's RenderPlayerEvent
 */
@Environment(EnvType.CLIENT)
public interface RenderPlayerCallback {
    class Context {
        private final PlayerRenderer renderer;
        private final AbstractClientPlayer entity;
        private final float entityYaw;
        private final float partialTicks;
        private final PoseStack poseStack;
        private final MultiBufferSource buffer;
        private final int packedLight;

        public Context(PlayerRenderer renderer, AbstractClientPlayer entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
            this.renderer = renderer;
            this.entity = entity;
            this.entityYaw = entityYaw;
            this.partialTicks = partialTicks;
            this.poseStack = poseStack;
            this.buffer = buffer;
            this.packedLight = packedLight;
        }

        public PlayerRenderer getRenderer() {
            return renderer;
        }

        public AbstractClientPlayer getEntity() {
            return entity;
        }

        public float getEntityYaw() {
            return entityYaw;
        }

        public float getPartialTicks() {
            return partialTicks;
        }

        public PoseStack getPoseStack() {
            return poseStack;
        }

        public MultiBufferSource getBuffer() {
            return buffer;
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

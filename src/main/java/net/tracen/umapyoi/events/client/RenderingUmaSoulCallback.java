package net.tracen.umapyoi.events.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.tracen.umapyoi.client.model.UmaPlayerModel;

@Environment(EnvType.CLIENT)
public interface RenderingUmaSoulCallback extends RenderingModelCallback {
    interface Pre extends RenderingUmaSoulCallback {
        Event<Pre> EVENT = EventFactory.createArrayBacked(Pre.class,
                (listeners) -> (context) -> {
                    for (Pre listener : listeners) {
                        if (listener.callback(context))
                            return true;
                    }

                    return false;
                });

        static boolean invoke(Context context) {
            return EVENT.invoker().callback(context);
        }
    }

    interface Post extends RenderingUmaSoulCallback {
        Event<Post> EVENT = EventFactory.createArrayBacked(Post.class,
                (listeners) -> (context) -> {
                    for (Post listener : listeners) {
                        if (listener.callback(context))
                            return true;
                    }

                    return false;
                });

        static boolean invoke(Context context) {
            return EVENT.invoker().callback(context);
        }
    }
}

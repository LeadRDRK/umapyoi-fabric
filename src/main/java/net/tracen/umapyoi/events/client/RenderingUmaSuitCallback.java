package net.tracen.umapyoi.events.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@Environment(EnvType.CLIENT)
public interface RenderingUmaSuitCallback extends RenderingModelCallback {
    interface Pre extends RenderingUmaSuitCallback {
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

    interface Post extends RenderingUmaSuitCallback {
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

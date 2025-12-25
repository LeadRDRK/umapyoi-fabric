package net.tracen.umapyoi.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.registry.factors.UmaFactorStack;

public interface ApplyFactorCallback {
    class Context {
        private final UmaFactorStack factor;
        private final ItemStack soul;

        public Context(UmaFactorStack stack, ItemStack soul) {
            this.factor = stack;
            this.soul = soul;
        }

        public UmaFactorStack getFactor() {
            return factor;
        }

        public ItemStack getUmaSoul() {
            return this.soul;
        }
    }

    interface Pre extends ApplyFactorCallback {
        /**
         * @return Whether to cancel the event
         */
        boolean callback(Context context);

        Event<Pre> EVENT = EventFactory.createArrayBacked(Pre.class,
                (listeners) -> (context) -> {
                    for (Pre listener : listeners) {
                        if (listener.callback(context)) return true;
                    }

                    return false;
                });

        static boolean invoke(Context context) {
            return EVENT.invoker().callback(context);
        }
    }

    interface Post extends ApplyFactorCallback {
        void callback(Context context);

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

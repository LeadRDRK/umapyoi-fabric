package net.tracen.umapyoi.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.registry.training.SupportStack;

public interface ApplyTrainingSupportCallback {
    class Context {
        private final SupportStack support;
        private final ItemStack soul;

        public Context(SupportStack stack, ItemStack soul) {
            this.support = stack;
            this.soul = soul;
        }

        public SupportStack getSupport() {
            return support;
        }

        public ItemStack getUmaSoul() {
            return this.soul;
        }
    }

    /**
     * @return Whether to cancel the event
     */
    boolean callback(Context context);

    interface Pre extends ApplyTrainingSupportCallback {
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

    interface Post extends ApplyTrainingSupportCallback {
        Event<Post> EVENT = EventFactory.createArrayBacked(Post.class,
                (listeners) -> (context) -> {
                    for (Post listener : listeners) {
                        if (listener.callback(context)) return true;
                    }

                    return false;
                });

        static boolean invoke(Context context) {
            return EVENT.invoker().callback(context);
        }
    }
}

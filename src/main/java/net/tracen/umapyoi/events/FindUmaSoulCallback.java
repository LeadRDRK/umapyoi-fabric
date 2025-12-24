package net.tracen.umapyoi.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface FindUmaSoulCallback {
    abstract class Context extends UmaSoulContext {
        private final LivingEntity owner;
        protected Context(LivingEntity entity, ItemStack soul) {
            super(soul);
            this.owner = entity;
        }

        public LivingEntity getLivingEntity() {
            return owner;
        }
    }

    interface Pre extends FindUmaSoulCallback {
        class Context extends FindUmaSoulCallback.Context {
            public Context(LivingEntity entity) {
                super(entity, ItemStack.EMPTY);
            }
        }

        void callback(Context context);

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

    interface Post extends FindUmaSoulCallback {
        class Context extends FindUmaSoulCallback.Context {
            public Context(LivingEntity entity, ItemStack soul) {
                super(entity, soul);
            }
        }

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

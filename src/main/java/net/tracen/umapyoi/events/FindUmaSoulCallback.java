package net.tracen.umapyoi.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface FindUmaSoulCallback {
    interface Pre extends FindUmaSoulCallback {
        ItemStack callback(LivingEntity entity);

        Event<Pre> EVENT = EventFactory.createArrayBacked(Pre.class,
                (listeners) -> (entity) -> {
                    var soul = ItemStack.EMPTY;
                    for (Pre listener : listeners) {
                        var res = listener.callback(entity);
                        if (!res.isEmpty())
                            soul = res;
                    }
                    return soul;
                });

        static ItemStack invoke(LivingEntity entity) {
            return EVENT.invoker().callback(entity);
        }
    }

    interface Post extends FindUmaSoulCallback {
        ItemStack callback(LivingEntity entity, ItemStack soul);

        Event<Post> EVENT = EventFactory.createArrayBacked(Post.class,
                (listeners) -> (entity, soul) -> {
                    for (Post listener : listeners) {
                        var res = listener.callback(entity, soul);
                        if (!res.isEmpty())
                            soul = res;
                    }
                    return soul;
                });

        static ItemStack invoke(LivingEntity entity, ItemStack soul) {
            return EVENT.invoker().callback(entity, soul);
        }
    }
}

package net.tracen.umapyoi.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class LivingEntityUseItemEvents {
    public interface Finish {
        void onUseItem(LivingEntity entity, ItemStack item);
    }

    public static final Event<Finish> FINISH = EventFactory.createArrayBacked(Finish.class,
            (listeners) -> (entity, item) -> {
                for (Finish listener : listeners) {
                    listener.onUseItem(entity, item);
                }
            });
}

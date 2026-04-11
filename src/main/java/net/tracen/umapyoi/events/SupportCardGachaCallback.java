package net.tracen.umapyoi.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public interface SupportCardGachaCallback {
    class Context extends GachaContext {
        public Context(ItemStack input, Collection<Identifier> fulfills, Identifier target,
                       ItemStack defaultResult, RandomSource src) {
            super(input, fulfills, target, defaultResult, src);
        }

        @Override
        public String getType() {
            return "supportcard";
        }
    }

    void callback(Context context);

    Event<SupportCardGachaCallback> EVENT = EventFactory.createArrayBacked(SupportCardGachaCallback.class,
            (listeners) -> (context) -> {
                for (SupportCardGachaCallback listener : listeners) {
                    listener.callback(context);
                }
            });

    static void invoke(Context context) {
        EVENT.invoker().callback(context);
    }
}

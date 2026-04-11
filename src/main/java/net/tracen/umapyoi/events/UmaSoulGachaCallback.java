package net.tracen.umapyoi.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public interface UmaSoulGachaCallback {
    class Context extends GachaContext {
        public Context(ItemStack input, Collection<Identifier> fulfills, Identifier target,
                       ItemStack defaultResult, RandomSource src) {
            super(input, fulfills, target, defaultResult, src);
        }

        @Override
        public String getType() {
            return "umasoul";
        }
    }

    void callback(Context context);

    Event<UmaSoulGachaCallback> EVENT = EventFactory.createArrayBacked(UmaSoulGachaCallback.class,
            (listeners) -> (context) -> {
                for (UmaSoulGachaCallback listener : listeners) {
                    listener.callback(context);
                }
            });

    static void invoke(Context context) {
        EVENT.invoker().callback(context);
    }
}

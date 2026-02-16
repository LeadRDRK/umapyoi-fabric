package net.tracen.umapyoi.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public interface LearnSkillCallback {
    class Context extends SkillContext {
        private final ItemStack umasoul;
        public Context(Identifier skill, ItemStack umasoul) {
            super(skill);
            this.umasoul = umasoul;
        }

        public ItemStack getUmaSoul() {
            return umasoul;
        }
    }

    void callback(Context context);

    Event<LearnSkillCallback> EVENT = EventFactory.createArrayBacked(LearnSkillCallback.class,
            (listeners) -> (context) -> {
                for (LearnSkillCallback listener : listeners) {
                    listener.callback(context);
                }
            });

    static void invoke(Context context) {
        EVENT.invoker().callback(context);
    }
}

package net.tracen.umapyoi.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface ApplySkillCallback {
    class Context extends SkillContext {
        private final Level level;
        private final Player user;

        public Context(ResourceLocation skill, Level level, Player user) {
            super(skill);
            this.level = level;
            this.user = user;
        }

        public Level getLevel() {
            return level;
        }

        public Player getPlayer() {
            return user;
        }
    }

    void callback(Context context);

    Event<ApplySkillCallback> EVENT = EventFactory.createArrayBacked(ApplySkillCallback.class,
            (listeners) -> (context) -> {
                for (ApplySkillCallback listener : listeners) {
                    listener.callback(context);
                }
            });

    static void invoke(Context context) {
        EVENT.invoker().callback(context);
    }
}

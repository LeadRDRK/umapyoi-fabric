package net.tracen.umapyoi.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.registry.UmaSkillRegistry;

public interface UseSkillCallback {
    class Context extends SkillContext {
        private final Level level;
        private final Player user;
        private int ap;

        public Context(ResourceLocation skill, Level level, Player user, int ap) {
            super(skill);
            this.level = level;
            this.user = user;
            this.ap = ap;
        }

        // Left for old api capabilities. DO NOT USE.
        @Deprecated
        public Context(ResourceLocation skill, Level level, Player user) {
            this(skill, level, user, UmaSkillRegistry.REGISTRY.get().get(skill).getActionPoint());
        }

        public Level getLevel() {
            return level;
        }

        public Player getPlayer() {
            return user;
        }

        public void setAp(int ap) {
            this.ap = ap;
        }

        public int getAp() {
            return this.ap;
        }
    }

    /**
     * @return Whether to cancel the event
     */
    boolean callback(Context context);

    Event<UseSkillCallback> EVENT = EventFactory.createArrayBacked(UseSkillCallback.class,
            (listeners) -> (context) -> {
                for (UseSkillCallback listener : listeners) {
                    if (listener.callback(context))
                        return true;
                }

                return false;
            });

    /**
     * @return Whether the event was cancelled
     */
    static boolean invoke(Context context) {
        return EVENT.invoker().callback(context);
    }
}

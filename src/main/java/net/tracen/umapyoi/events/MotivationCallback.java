package net.tracen.umapyoi.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.registry.umadata.Motivations;

import java.util.Objects;

public interface MotivationCallback {
    class Context {
        public final Motivations previous;
        private final Motivations afterDefault;
        private Motivations after;
        private final boolean doTriggerBonusDefault;
        public Boolean doTriggerBonus;
        private final ItemStack targetSoul;
        private final LivingEntity target;

        public Context(Motivations previous, Motivations after, boolean doTriggerBonus, ItemStack soul, LivingEntity target) {
            this.previous = previous;
            this.afterDefault = after;
            this.doTriggerBonus = doTriggerBonus;
            this.doTriggerBonusDefault = doTriggerBonus;
            this.targetSoul = soul;
            this.target = target;
        }

        public Motivations getAfter() {
            return Objects.requireNonNullElse(this.after, this.afterDefault);
        }

        public void setAfter(Motivations motiv) {
            this.after = motiv;
        }

        public boolean getDoTriggerBonus() {
            return Objects.requireNonNullElse(doTriggerBonus, doTriggerBonusDefault);
        }

        public void setDoTriggerBonus(Boolean trigger) {
            this.doTriggerBonus = trigger;
        }

        public ItemStack getTargetSoul() {
            return this.targetSoul;
        }

        public LivingEntity getTarget() {
            return this.target;
        }
    }

    /**
     * @return true to cancel the event, false to continue
     */
    boolean callback(Context context);

    Event<MotivationCallback> EVENT = EventFactory.createArrayBacked(MotivationCallback.class,
            (listeners) -> (context) -> {
                for (MotivationCallback listener : listeners) {
                    if (listener.callback(context))
                        return true;
                }

                return false;
            });

    /**
     * @return true if the event was cancelled
     */
    static boolean invoke(Context context) {
        return EVENT.invoker().callback(context);
    }
}

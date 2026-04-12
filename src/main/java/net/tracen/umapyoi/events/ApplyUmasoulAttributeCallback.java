package net.tracen.umapyoi.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.item.UmaSoulItem;
import net.tracen.umapyoi.utils.UmaStatusUtils;

import java.util.function.BiConsumer;

import eu.pb4.trinkets.api.TrinketSlotAccess;

public interface ApplyUmasoulAttributeCallback {
    class Context extends UmaSoulContext {
        private final TrinketSlotAccess slotAccess;
        private final Identifier slotIdentifier;
        private final BiConsumer<Holder<Attribute>, AttributeModifier> consumer;
        private final LivingEntity user;
        public Context(LivingEntity user, ItemStack soul, TrinketSlotAccess slotAccess, Identifier slotIdentifier, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
            super(soul);
            this.user = user;
            this.slotAccess = slotAccess;
            this.slotIdentifier = slotIdentifier;
            this.consumer = consumer;
        }

        public TrinketSlotAccess getSlotAccess() {
            return slotAccess;
        }

        public Identifier getSlotIdentifier() {
            return slotIdentifier;
        }

        public BiConsumer<Holder<Attribute>, AttributeModifier> getConsumer() {
            return consumer;
        }

        @Override
        public void setUmaSoul(ItemStack soul) {
            throw new UnsupportedOperationException("Tried to set a new soul for ApplyUmasoulAttributeEvent");
        }

        public double getExactProperty(UmaStatusUtils.StatusType status, double limit) {
            return getExactProperty(this.user, status, limit);
        }

        public double getExactProperty(LivingEntity user, UmaStatusUtils.StatusType status, double limit) {
            return UmaSoulItem.getExactProperty(soul, user, status, limit);
        }
    }

    void callback(Context context);

    Event<ApplyUmasoulAttributeCallback> EVENT = EventFactory.createArrayBacked(ApplyUmasoulAttributeCallback.class,
            (listeners) -> (context) -> {
                for (ApplyUmasoulAttributeCallback listener : listeners) {
                    listener.callback(context);
                }
            });

    static void invoke(Context context) {
        EVENT.invoker().callback(context);
    }
}

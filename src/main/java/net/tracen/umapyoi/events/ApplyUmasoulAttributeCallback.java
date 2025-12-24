package net.tracen.umapyoi.events;

import com.google.common.collect.Multimap;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

import dev.emi.trinkets.api.SlotReference;

public interface ApplyUmasoulAttributeCallback {
    class Context extends UmaSoulContext {
        private final SlotReference slotReference;
        private final UUID uuid;
        private final Multimap<Attribute, AttributeModifier> atts;
        public Context(ItemStack soul, SlotReference slotReference, UUID uuid, Multimap<Attribute, AttributeModifier> atts) {
            super(soul);
            this.uuid = uuid;
            this.slotReference = slotReference;
            this.atts = atts;
        }

        public SlotReference slotReference() {
            return slotReference;
        }

        public UUID getUUID() {
            return uuid;
        }

        public Multimap<Attribute, AttributeModifier> getAttributes() {
            return atts;
        }

        @Override
        public void setUmaSoul(ItemStack soul) {
            throw new UnsupportedOperationException("Tried to set a new soul for ApplyUmasoulAttributeEvent");
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

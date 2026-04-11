package net.tracen.umapyoi.events;

import com.google.common.collect.Multimap;

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

import dev.emi.trinkets.api.SlotReference;

public interface ApplyUmasoulAttributeCallback {
    class Context extends UmaSoulContext {
        private final SlotReference slotReference;
        private final Identifier slotIdentifier;
        private final Multimap<Holder<Attribute>, AttributeModifier> atts;
        private final LivingEntity user;
        public Context(LivingEntity user, ItemStack soul, SlotReference slotReference, Identifier slotIdentifier, Multimap<Holder<Attribute>, AttributeModifier> atts) {
            super(soul);
            this.slotIdentifier = slotIdentifier;
            this.slotReference = slotReference;
            this.atts = atts;
            this.user = user;
        }

        public SlotReference slotReference() {
            return slotReference;
        }

        public Identifier getSlotIdentifier() {
            return slotIdentifier;
        }

        public Multimap<Holder<Attribute>, AttributeModifier> getAttributes() {
            return atts;
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

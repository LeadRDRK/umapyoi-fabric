package net.tracen.umapyoi.advancements.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.ExtraCodecs;
import net.tracen.umapyoi.Umapyoi;

import java.util.Optional;

public class GrantBookOnFirstJoin extends SimpleCriterionTrigger<GrantBookOnFirstJoin.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(Umapyoi.MODID, "grant_book_on_first_join");


    @Override
    public Codec<Instance> codec() {
        return null;
    }

    public record Instance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance
                .group(ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(Instance::player))
                .apply(instance, Instance::new));

        public boolean test(ServerPlayer player) {
            return Umapyoi.CONFIG.GRANT_GUIDE_ON_FIRST_JOIN();
        }
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, (I) -> I.test(player));
    }

    public static class PlayerJoinListener implements ServerPlayConnectionEvents.Join {
        @Override
        public void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
            TriggerRegistry.GRANT_BOOK_ON_FIRST_JOIN.trigger(handler.getPlayer());
        }

        public static void register() {
            ServerPlayConnectionEvents.JOIN.register(new PlayerJoinListener());
        }
    }
}

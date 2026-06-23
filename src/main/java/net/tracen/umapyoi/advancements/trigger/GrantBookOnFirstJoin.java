package net.tracen.umapyoi.advancements.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.tracen.umapyoi.Umapyoi;

import org.jspecify.annotations.NullMarked;

import java.util.Optional;

@NullMarked
public class GrantBookOnFirstJoin extends SimpleCriterionTrigger<GrantBookOnFirstJoin.Instance> {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(Umapyoi.MODID, "grant_book_on_first_join");

    @Override
    public Codec<Instance> codec() {
        return MapCodec.unitCodec(Instance::new);
    }

    public static class Instance implements SimpleCriterionTrigger.SimpleInstance {
        public boolean test(ServerPlayer player) {
            return Umapyoi.CONFIG.GRANT_GUIDE_ON_FIRST_JOIN;
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return Optional.empty();
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

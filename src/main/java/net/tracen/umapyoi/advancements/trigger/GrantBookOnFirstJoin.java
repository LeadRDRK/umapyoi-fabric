package net.tracen.umapyoi.advancements.trigger;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.tracen.umapyoi.Umapyoi;

import java.util.Optional;

@MethodsReturnNonnullByDefault
public class GrantBookOnFirstJoin extends SimpleCriterionTrigger<GrantBookOnFirstJoin.Instance> {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "grant_book_on_first_join");

    @Override
    public Codec<Instance> codec() {
        return Codec.unit(Instance::new);
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

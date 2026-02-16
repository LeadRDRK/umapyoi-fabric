package net.tracen.umapyoi.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.container.UmaSelectMenu;

import org.jspecify.annotations.NullMarked;

public record EmptyResultPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<EmptyResultPacket> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Umapyoi.MODID, "packet/empty_result"));

    public static final StreamCodec<RegistryFriendlyByteBuf, EmptyResultPacket> CODEC =
            StreamCodec.unit(new EmptyResultPacket());

    @Override
    @NullMarked
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(EmptyResultPacket packet, ServerPlayNetworking.Context context) {
        var player = context.player();
        if (player.containerMenu instanceof UmaSelectMenu menu) {
            menu.setItemName(null);
        }
    }
}

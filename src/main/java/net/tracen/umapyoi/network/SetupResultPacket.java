package net.tracen.umapyoi.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.container.IItemNameMutableMenu;

import org.jspecify.annotations.NullMarked;

public record SetupResultPacket(String message) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SetupResultPacket> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Umapyoi.MODID, "packet/setup_result"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SetupResultPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SetupResultPacket::message,
            SetupResultPacket::new
    );

    @Override
    @NullMarked
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(SetupResultPacket packet, ServerPlayNetworking.Context context) {
        var player = context.player();
        if (player.containerMenu instanceof IItemNameMutableMenu menu) {
            String s = packet.message;
            if (s.length() <= 50) {
                menu.setItemName(Identifier.tryParse(s));
            }
        }
    }
}

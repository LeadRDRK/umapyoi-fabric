package net.tracen.umapyoi.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.container.UmaSelectMenu;

public record SetupResultPacket(String message) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SetupResultPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "packet/setup_result"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SetupResultPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SetupResultPacket::message,
            SetupResultPacket::new
    );

    @Override
    @MethodsReturnNonnullByDefault
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(SetupResultPacket packet, ServerPlayNetworking.Context context) {
        var player = context.player();
        if (player.containerMenu instanceof UmaSelectMenu menu) {
            String s = packet.message;
            if (s.length() <= 50) {
                menu.setItemName(ResourceLocation.tryParse(s));
            }
        }
    }
}

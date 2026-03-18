package net.tracen.umapyoi.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.container.IItemNameMutableMenu;

public class EmptyResultPacket implements FabricPacket {
    public EmptyResultPacket() {
    }

    @Override
    public void write(FriendlyByteBuf buf) {
    }

    public static final PacketType<EmptyResultPacket> TYPE = PacketType.create(
            new ResourceLocation(Umapyoi.MODID, "packet/empty_result"),
            (buf) -> new EmptyResultPacket()
    );

    @Override
    public PacketType<EmptyResultPacket> getType() {
        return TYPE;
    }

    public static void handler(EmptyResultPacket packet, ServerPlayer player, PacketSender responseSender) {
        if (player.containerMenu instanceof IItemNameMutableMenu menu) {
            menu.setItemName(null);
        }
    }
}

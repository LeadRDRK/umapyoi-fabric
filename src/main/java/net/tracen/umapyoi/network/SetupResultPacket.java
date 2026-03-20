package net.tracen.umapyoi.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.SharedConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.container.IItemNameMutableMenu;

public class SetupResultPacket implements FabricPacket {
    private final String message;

    public SetupResultPacket(String message) {
        this.message = message;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(message);
    }

    public static final PacketType<SetupResultPacket> TYPE = PacketType.create(
            new ResourceLocation(Umapyoi.MODID, "packet/setup_result"),
            (buf) -> new SetupResultPacket(
                    buf.readUtf()
            )
    );

    @Override
    public PacketType<SetupResultPacket> getType() {
        return TYPE;
    }

    public static void handler(SetupResultPacket packet, ServerPlayer player, PacketSender responseSender) {
        if (player.containerMenu instanceof IItemNameMutableMenu menu) {
            String s = SharedConstants.filterText(packet.message);
            //Umapyoi.getLogger().info("Packet received:{}",s);
            if (s.length() <= 50) {
                menu.setItemName(ResourceLocation.tryParse(s));
            }
        }
    }
}

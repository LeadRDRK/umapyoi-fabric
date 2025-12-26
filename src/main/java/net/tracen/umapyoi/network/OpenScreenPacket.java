package net.tracen.umapyoi.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.tracen.umapyoi.Umapyoi;

import io.netty.buffer.Unpooled;

public class OpenScreenPacket implements FabricPacket {
    final int typeId;
    final int syncId;
    final Component title;
    final FriendlyByteBuf extraData;

    public OpenScreenPacket(int typeId, int syncId, Component title, FriendlyByteBuf extraData) {
        this.typeId = typeId;
        this.syncId = syncId;
        this.title = title;
        this.extraData = extraData;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(typeId);
        buf.writeVarInt(syncId);
        buf.writeComponent(title);
        buf.writeVarInt(extraData.readableBytes());
        buf.writeBytes(extraData, extraData.readerIndex(), extraData.readableBytes());
    }

    public static final PacketType<OpenScreenPacket> TYPE = PacketType.create(
            new ResourceLocation(Umapyoi.MODID, "packet/open_screen"),
            (buf) -> new OpenScreenPacket(
                    buf.readVarInt(),
                    buf.readVarInt(),
                    buf.readComponent(),
                    new FriendlyByteBuf(Unpooled.wrappedBuffer(buf.readByteArray(32600)))
            )
    );

    @Override
    public PacketType<OpenScreenPacket> getType() {
        return TYPE;
    }

    @Environment(EnvType.CLIENT)
    public static void handler(OpenScreenPacket packet, LocalPlayer player, PacketSender responseSender) {
        MenuType<?> type = BuiltInRegistries.MENU.byId(packet.typeId);
        if (type == null) {
            Umapyoi.getLogger().error("Unknown menu type ID: " + packet.typeId);
            return;
        }
        if (!(type instanceof ExtendedScreenHandlerType<?>)) {
            Umapyoi.getLogger().error("Attempt to open non-extended screen handler: {}", packet.typeId);
            return;
        }

        Minecraft client = Minecraft.getInstance();
        Inventory inventory = client.player.getInventory();
        packet.extraData.readerIndex(0); // is this even needed?
        AbstractContainerMenu menu = ((ExtendedScreenHandlerType<?>)type).create(packet.syncId, inventory, packet.extraData);
        @SuppressWarnings("unchecked")
        Screen screen = ((MenuScreens.ScreenConstructor<AbstractContainerMenu, ?>) MenuScreens.getConstructor(type)).create(menu, inventory, packet.title);
        client.player.containerMenu = ((MenuAccess<?>) screen).getMenu();
        client.setScreen(screen);
    }
}

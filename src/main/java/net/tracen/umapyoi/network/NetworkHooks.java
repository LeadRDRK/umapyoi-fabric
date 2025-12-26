package net.tracen.umapyoi.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.tracen.umapyoi.mixin.ServerPlayerAccessor;

import java.util.function.Consumer;

import io.netty.buffer.Unpooled;

public class NetworkHooks {
    public static void openScreen(ServerPlayer player, MenuProvider containerProvider, BlockPos pos) {
        openScreen(player, containerProvider, buf -> buf.writeBlockPos(pos));
    }

    public static void openScreen(ServerPlayer player, MenuProvider factory, Consumer<FriendlyByteBuf> extraDataWriter) {
        player.doCloseContainer();
        ((ServerPlayerAccessor) player).invokeNextContainerCounter();
        int syncId = ((ServerPlayerAccessor) player).getContainerCounter();

        FriendlyByteBuf extraData = new FriendlyByteBuf(Unpooled.buffer());
        extraDataWriter.accept(extraData);
        extraData.readerIndex(0);
        AbstractContainerMenu menu = factory.createMenu(syncId, player.getInventory(), player);
        ServerPlayNetworking.send(player, new OpenScreenPacket(
                BuiltInRegistries.MENU.getId(menu.getType()),
                syncId,
                factory.getDisplayName(),
                extraData
        ));

        player.containerMenu = menu;
        ((ServerPlayerAccessor) player).invokeInitMenu(menu);
    }
}

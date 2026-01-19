package net.tracen.umapyoi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.tracen.umapyoi.client.screen.ScreensRegistry;
import net.tracen.umapyoi.events.client.RenderArmCallback;
import net.tracen.umapyoi.events.client.RenderPlayerCallback;
import net.tracen.umapyoi.events.client.RenderingUmaSoulCallback;
import net.tracen.umapyoi.events.handler.ClientEvents;
import net.tracen.umapyoi.events.handler.ClientSetupEvents;
import net.tracen.umapyoi.network.OpenScreenPacket;

public class UmapyoiClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        UmapyoiCreativeGroup.register();

        // ClientEvents
        RenderingUmaSoulCallback.Pre.EVENT.register(ClientEvents::preUmaSoulRendering);
        RenderPlayerCallback.Pre.EVENT.register(ClientEvents::onPlayerRendering);
        RenderPlayerCallback.Post.EVENT.register(ClientEvents::onPlayerRenderingPost);
        RenderArmCallback.EVENT.register(ClientEvents::onPlayerArmRendering);

        // ClientSetupEvents
        ClientSetupEvents.setupClient();

        // ScreensRegistry
        ScreensRegistry.register();

        // Networking
        ClientPlayNetworking.registerGlobalReceiver(OpenScreenPacket.TYPE, OpenScreenPacket::handler);
    }
}

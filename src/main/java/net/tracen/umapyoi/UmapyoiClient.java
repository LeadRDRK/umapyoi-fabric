package net.tracen.umapyoi;

import net.fabricmc.api.ClientModInitializer;
import net.tracen.umapyoi.block.RaceRegisterBlock;
import net.tracen.umapyoi.client.screen.ScreensRegistry;
import net.tracen.umapyoi.events.client.RenderArmCallback;
import net.tracen.umapyoi.events.client.RenderingUmaSoulCallback;
import net.tracen.umapyoi.events.handler.ClientEvents;
import net.tracen.umapyoi.events.handler.ClientSetupEvents;

public class UmapyoiClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        UmapyoiCreativeGroup.register();

        // ClientEvents
        RenderingUmaSoulCallback.Pre.EVENT.register(ClientEvents::preUmaSoulRendering);
        RenderArmCallback.EVENT.register(ClientEvents::onPlayerArmRendering);

        // ClientSetupEvents
        ClientSetupEvents.setupClient();

        // ScreensRegistry
        ScreensRegistry.register();

        RaceRegisterBlock.registerBlockColor();
    }
}

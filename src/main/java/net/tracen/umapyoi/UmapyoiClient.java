package net.tracen.umapyoi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.PictureInPictureRendererRegistry;
import net.tracen.umapyoi.client.screen.ScreensRegistry;
import net.tracen.umapyoi.client.screen.pip.GuiBedrockModelRenderer;
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

        // PIP model renderer
        PictureInPictureRendererRegistry.register(GuiBedrockModelRenderer::new);
    }
}

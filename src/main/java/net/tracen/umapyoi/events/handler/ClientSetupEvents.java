package net.tracen.umapyoi.events.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.server.packs.PackType;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.block.entity.BlockEntityRegistry;
import net.tracen.umapyoi.client.ActionBarOverlay;
import net.tracen.umapyoi.client.MotivationOverlay;
import net.tracen.umapyoi.client.SkillOverlay;
import net.tracen.umapyoi.client.key.SkillKeyMapping;
import net.tracen.umapyoi.client.model.BedrockModelResourceLoader;
import net.tracen.umapyoi.client.renderer.blockentity.SilverSupportAlbumPedestalBlockRender;
import net.tracen.umapyoi.client.renderer.blockentity.SilverUmaPedestalBlockRender;
import net.tracen.umapyoi.client.renderer.blockentity.SupportAlbumPedestalBlockRender;
import net.tracen.umapyoi.client.renderer.blockentity.ThreeGoddessBlockRender;
import net.tracen.umapyoi.client.renderer.blockentity.UmaPedestalBlockRender;
import net.tracen.umapyoi.client.renderer.blockentity.UmaStatuesBlockRender;

@Environment(EnvType.CLIENT)
public class ClientSetupEvents {
    public static void setupClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.TRAINING_FACILITY.get(), RenderType.cutoutMipped());

        ClientTickEvents.END_CLIENT_TICK.register(SkillKeyMapping::onEndClientTick);

        HudRenderCallback.EVENT.register(new ActionBarOverlay());
        HudRenderCallback.EVENT.register(new MotivationOverlay());
        HudRenderCallback.EVENT.register(new SkillOverlay());

        BlockEntityRenderers.register(BlockEntityRegistry.THREE_GODDESS.get(), ThreeGoddessBlockRender::new);
        BlockEntityRenderers.register(BlockEntityRegistry.UMA_PEDESTAL.get(), UmaPedestalBlockRender::new);
        BlockEntityRenderers.register(BlockEntityRegistry.SUPPORT_ALBUM_PEDESTAL.get(),
                SupportAlbumPedestalBlockRender::new);
        
        BlockEntityRenderers.register(BlockEntityRegistry.UMA_STATUES.get(), UmaStatuesBlockRender::new);
        
        BlockEntityRenderers.register(BlockEntityRegistry.SILVER_UMA_PEDESTAL.get(), SilverUmaPedestalBlockRender::new);
        BlockEntityRenderers.register(BlockEntityRegistry.SILVER_SUPPORT_ALBUM_PEDESTAL.get(),
                SilverSupportAlbumPedestalBlockRender::new);

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new BedrockModelResourceLoader("models/umapyoi"));
    }
}

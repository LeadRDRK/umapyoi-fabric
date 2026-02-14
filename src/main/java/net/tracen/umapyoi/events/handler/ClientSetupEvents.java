package net.tracen.umapyoi.events.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.packs.PackType;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.block.entity.BlockEntityRegistry;
import net.tracen.umapyoi.client.ActionBarOverlay;
import net.tracen.umapyoi.client.MotivationOverlay;
import net.tracen.umapyoi.client.SkillOverlay;
import net.tracen.umapyoi.client.key.SkillKeyMapping;
import net.tracen.umapyoi.client.model.BedrockModelResourceLoader;
import net.tracen.umapyoi.client.model.UmaCostumeItemModel;
import net.tracen.umapyoi.client.renderer.blockentity.SilverSupportAlbumPedestalBlockRender;
import net.tracen.umapyoi.client.renderer.blockentity.SilverUmaPedestalBlockRender;
import net.tracen.umapyoi.client.renderer.blockentity.SupportAlbumPedestalBlockRender;
import net.tracen.umapyoi.client.renderer.blockentity.ThreeGoddessBlockRender;
import net.tracen.umapyoi.client.renderer.blockentity.UmaPedestalBlockRender;
import net.tracen.umapyoi.client.renderer.blockentity.UmaStatuesBlockRender;
import net.tracen.umapyoi.item.AbstractSuitItem;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.UmaSoulItem;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ClientSetupEvents {
    public static void setupClient() {
        registerKeyBinds();
        registerGuiOverlay();
        registerModelLoadingPlugin();

        // Trinkets renderers
        UmaSoulItem.registerRenderer();
        AbstractSuitItem.registerRenderer(ItemRegistry.SUMMER_UNIFORM);
        AbstractSuitItem.registerRenderer(ItemRegistry.WINTER_UNIFORM);
        AbstractSuitItem.registerRenderer(ItemRegistry.TRAINING_SUIT);
        AbstractSuitItem.registerRenderer(ItemRegistry.SWIMSUIT);
        AbstractSuitItem.registerRenderer(ItemRegistry.UMA_COSTUME);

        BlockRenderLayerMap.putBlock(BlockRegistry.TRAINING_FACILITY, ChunkSectionLayer.CUTOUT_MIPPED);

        ClientTickEvents.END_CLIENT_TICK.register(SkillKeyMapping::onEndClientTick);

        // Block entities
        BlockEntityRenderers.register(BlockEntityRegistry.THREE_GODDESS.get(), ThreeGoddessBlockRender::new);
        BlockEntityRenderers.register(BlockEntityRegistry.UMA_PEDESTAL.get(), UmaPedestalBlockRender::new);
        BlockEntityRenderers.register(BlockEntityRegistry.SUPPORT_ALBUM_PEDESTAL.get(),
                SupportAlbumPedestalBlockRender::new);
        
        BlockEntityRenderers.register(BlockEntityRegistry.UMA_STATUES.get(), UmaStatuesBlockRender::new);
        
        BlockEntityRenderers.register(BlockEntityRegistry.SILVER_UMA_PEDESTAL.get(), SilverUmaPedestalBlockRender::new);
        BlockEntityRenderers.register(BlockEntityRegistry.SILVER_SUPPORT_ALBUM_PEDESTAL.get(),
                SilverSupportAlbumPedestalBlockRender::new);

        // resourceLoadingListener
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
                .registerReloadListener(new BedrockModelResourceLoader("models/umapyoi"));
    }

    public static void registerModelLoadingPlugin() {
        ModelLoadingPlugin.register(pluginContext -> {
            UmaCostumeItemModel.onModelLoading(pluginContext);

            pluginContext.modifyItemModelAfterBake().register(ClientSetupEvents::onBakedItemModel);
        });
    }

    public static ItemModel onBakedItemModel(ItemModel itemModel, ModelModifier.AfterBakeItem.Context context) {
        if (Objects.equals(context.itemId(), BuiltInRegistries.ITEM.getKey(ItemRegistry.UMA_COSTUME))) {
            return new UmaCostumeItemModel(itemModel);
        }

        return itemModel;
    }

    public static void registerKeyBinds() {
        KeyBindingHelper.registerKeyBinding(SkillKeyMapping.KEY_USE_SKILL);
        KeyBindingHelper.registerKeyBinding(SkillKeyMapping.KEY_FORMER_SKILL);
        KeyBindingHelper.registerKeyBinding(SkillKeyMapping.KEY_LATTER_SKILL);
    }

    public static void registerGuiOverlay() {
        HudRenderCallback.EVENT.register(new ActionBarOverlay());
        HudRenderCallback.EVENT.register(new MotivationOverlay());
        HudRenderCallback.EVENT.register(new SkillOverlay());
    }
}

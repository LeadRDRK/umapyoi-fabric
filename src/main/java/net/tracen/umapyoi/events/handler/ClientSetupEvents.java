package net.tracen.umapyoi.events.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.block.entity.BlockEntityRegistry;
import net.tracen.umapyoi.client.ActionBarOverlay;
import net.tracen.umapyoi.client.MotivationOverlay;
import net.tracen.umapyoi.client.SkillOverlay;
import net.tracen.umapyoi.client.key.SkillKeyMapping;
import net.tracen.umapyoi.client.model.BedrockModelResourceLoader;
import net.tracen.umapyoi.client.model.DynamicItemBakedModel;
import net.tracen.umapyoi.client.model.SupportCardItemModel;
import net.tracen.umapyoi.client.model.UmaCostumeItemModel;
import net.tracen.umapyoi.client.model.UmaRaceTicketItemModel;
import net.tracen.umapyoi.client.renderer.blockentity.GateRender;
import net.tracen.umapyoi.client.renderer.blockentity.SilverSupportAlbumPedestalBlockRender;
import net.tracen.umapyoi.client.renderer.blockentity.SilverUmaPedestalBlockRender;
import net.tracen.umapyoi.client.renderer.blockentity.SupportAlbumPedestalBlockRender;
import net.tracen.umapyoi.client.renderer.blockentity.ThreeGoddessBlockRender;
import net.tracen.umapyoi.client.renderer.blockentity.UmaPedestalBlockRender;
import net.tracen.umapyoi.client.renderer.blockentity.UmaStatuesBlockRender;
import net.tracen.umapyoi.item.AbstractSuitItem;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.UmaSoulItem;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class ClientSetupEvents {
    public static void setupClient() {
        registerKeyBinds();
        registerGuiOverlay();
        registerModelLoadingPlugin();

        // Trinkets renderers
        UmaSoulItem.registerRenderer();
        AbstractSuitItem.registerRenderer(ItemRegistry.SUMMER_UNIFORM.get());
        AbstractSuitItem.registerRenderer(ItemRegistry.WINTER_UNIFORM.get());
        AbstractSuitItem.registerRenderer(ItemRegistry.TRAINING_SUIT.get());
        AbstractSuitItem.registerRenderer(ItemRegistry.SWIMSUIT.get());
        AbstractSuitItem.registerRenderer(ItemRegistry.UMA_COSTUME.get());

        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.TRAINING_FACILITY.get(), RenderType.cutoutMipped());

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
        BlockEntityRenderers.register(BlockEntityRegistry.GATE.get(), GateRender::new);

        // resourceLoadingListener
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
                .registerReloadListener(new BedrockModelResourceLoader("models/umapyoi"));
    }

    public static void registerModelLoadingPlugin() {
        ModelLoadingPlugin.register(pluginContext -> {
            Stream.of("costume", "race_ticket", "support_card").forEachOrdered((type) -> {
                FileToIdConverter.json("models/item/" + type)
                        .listMatchingResources(Minecraft.getInstance().getResourceManager())
                        .keySet()
                        .stream()
                        .map(ClientSetupEvents::resolveModelLocation)
                        .forEach(pluginContext::addModels);
            });

            var afterBakeEvent = pluginContext.modifyModelAfterBake();

            afterBakeEvent.register(new BakedModelHandler(ItemRegistry.UMA_COSTUME.getId(),
                    UmaCostumeItemModel::new));

            afterBakeEvent.register(new BakedModelHandler(ItemRegistry.UMA_RACE_TICKET.getId(),
                    UmaRaceTicketItemModel::new));

            afterBakeEvent.register(new BakedModelHandler(ItemRegistry.SUPPORT_CARD.getId(),
                    SupportCardItemModel::new));
        });
    }

    private static ResourceLocation resolveModelLocation(ResourceLocation location) {
        return ResourceLocation.fromNamespaceAndPath(location.getNamespace(),
                location.getPath().substring(
                        "models/".length(),
                        location.getPath().length() - ".json".length()
                )
        );
    }

    private record BakedModelHandler(
            ResourceLocation namespace,
            BiFunction<BakedModel, ModelBakery, DynamicItemBakedModel> constructor
    ) implements ModelModifier.AfterBake {
        @Override
        public @Nullable BakedModel modifyModelAfterBake(@Nullable BakedModel bakedModel, Context context) {
            ModelResourceLocation origin = new ModelResourceLocation(namespace, "inventory");
            if (Objects.equals(context.topLevelId(), origin) || Objects.equals(context.resourceId(), origin.id())) {
                return constructor.apply(bakedModel, context.loader());
            }

            return bakedModel;
        }
    }

    public static void registerKeyBinds() {
        KeyBindingHelper.registerKeyBinding(SkillKeyMapping.KEY_USE_SKILL);
        KeyBindingHelper.registerKeyBinding(SkillKeyMapping.KEY_FORMER_SKILL);
        KeyBindingHelper.registerKeyBinding(SkillKeyMapping.KEY_LATTER_SKILL);
        KeyBindingHelper.registerKeyBinding(SkillKeyMapping.KEY_CONFIGURE_GUI);
    }

    public static void registerGuiOverlay() {
        HudRenderCallback.EVENT.register(new ActionBarOverlay());
        HudRenderCallback.EVENT.register(new MotivationOverlay());
        HudRenderCallback.EVENT.register(new SkillOverlay());
    }
}

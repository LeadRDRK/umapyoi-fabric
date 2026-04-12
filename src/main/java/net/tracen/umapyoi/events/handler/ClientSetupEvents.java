package net.tracen.umapyoi.events.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
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
import net.tracen.umapyoi.client.model.UnbakedExtraItemModel;
import net.tracen.umapyoi.client.renderer.blockentity.GateRender;
import net.tracen.umapyoi.client.renderer.blockentity.SupportAlbumPedestalBlockRenderer;
import net.tracen.umapyoi.client.renderer.blockentity.ThreeGoddessBlockRenderer;
import net.tracen.umapyoi.client.renderer.blockentity.UmaPedestalBlockRenderer;
import net.tracen.umapyoi.client.renderer.blockentity.UmaStatueBlockRenderer;
import net.tracen.umapyoi.item.AbstractSuitItem;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.UmaSoulItem;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

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

        ClientTickEvents.END_CLIENT_TICK.register(SkillKeyMapping::onEndClientTick);

        // Block entities
        BlockEntityRenderers.register(BlockEntityRegistry.THREE_GODDESS.get(), ThreeGoddessBlockRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegistry.UMA_PEDESTAL.get(), UmaPedestalBlockRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegistry.SUPPORT_ALBUM_PEDESTAL.get(),
                SupportAlbumPedestalBlockRenderer::new);
        
        BlockEntityRenderers.register(BlockEntityRegistry.UMA_STATUES.get(), UmaStatueBlockRenderer::new);
        
        BlockEntityRenderers.register(BlockEntityRegistry.SILVER_UMA_PEDESTAL.get(), UmaPedestalBlockRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegistry.SILVER_SUPPORT_ALBUM_PEDESTAL.get(),
                SupportAlbumPedestalBlockRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegistry.GATE.get(), GateRender::new);

        // resourceLoadingListener
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
                .registerReloadListener(new BedrockModelResourceLoader("models/umapyoi"));
    }

    public static void registerModelLoadingPlugin() {
        ModelLoadingPlugin.register(pluginContext -> {
            DynamicItemBakedModel.MODELS.clear();

            forEachDynamicItemModelResource((location, _) -> {
                var model = new UnbakedExtraItemModel(location);
                var modelId = Identifier.fromNamespaceAndPath(
                        location.getNamespace(),
                        location.getPath().substring("item/".length())
                );
                var key = ExtraModelKey.<ItemModel>create(location::toString);

                pluginContext.addModel(key, model);
                DynamicItemBakedModel.MODELS.put(modelId, key);
            });

            var afterBakeEvent = pluginContext.modifyItemModelAfterBake();

            afterBakeEvent.register(new BakedModelHandler(BuiltInRegistries.ITEM.getKey(ItemRegistry.UMA_COSTUME),
                    UmaCostumeItemModel::new));

            afterBakeEvent.register(new BakedModelHandler(BuiltInRegistries.ITEM.getKey(ItemRegistry.UMA_RACE_TICKET),
                    UmaRaceTicketItemModel::new));

            afterBakeEvent.register(new BakedModelHandler(BuiltInRegistries.ITEM.getKey(ItemRegistry.SUPPORT_CARD),
                    SupportCardItemModel::new));
        });
    }

    public static void forEachDynamicItemModelResource(BiConsumer<Identifier, Resource> consumer) {
        Stream.of("costume", "race_ticket", "support_card").forEachOrdered((type) -> {
            FileToIdConverter.json("models/item/" + type)
                    .listMatchingResources(Minecraft.getInstance().getResourceManager())
                    .forEach((resLocation, resource) -> {
                        consumer.accept(ClientSetupEvents.resolveModelLocation(resLocation), resource);
                    });
        });
    }

    private static Identifier resolveModelLocation(Identifier location) {
        return Identifier.fromNamespaceAndPath(location.getNamespace(),
                location.getPath().substring(
                        "models/".length(),
                        location.getPath().length() - ".json".length()
                )
        );
    }

    private record BakedModelHandler(
            Identifier id,
            Function<ItemModel, DynamicItemBakedModel> constructor
    ) implements ModelModifier.AfterBakeItem {
        @Override
        public ItemModel modifyModelAfterBake(ItemModel model, Context context) {
            if (Objects.equals(context.itemId(), this.id)) {
                return constructor.apply(model);
            }

            return model;
        }
    }

    public static void registerKeyBinds() {
        KeyMappingHelper.registerKeyMapping(SkillKeyMapping.KEY_USE_SKILL);
        KeyMappingHelper.registerKeyMapping(SkillKeyMapping.KEY_FORMER_SKILL);
        KeyMappingHelper.registerKeyMapping(SkillKeyMapping.KEY_LATTER_SKILL);
        KeyMappingHelper.registerKeyMapping(SkillKeyMapping.KEY_CONFIGURE_GUI);
    }

    public static void registerGuiOverlay() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS,
                ActionBarOverlay.ID, new ActionBarOverlay());
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS,
                MotivationOverlay.ID, new MotivationOverlay());
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS,
                SkillOverlay.ID, new SkillOverlay());
    }
}

package net.tracen.umapyoi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.client.key.SkillKeyMapping;
import net.tracen.umapyoi.client.screen.ScreensRegistry;
import net.tracen.umapyoi.events.client.RenderArmCallback;
import net.tracen.umapyoi.events.client.RenderPlayerCallback;
import net.tracen.umapyoi.events.handler.ClientEvents;
import net.tracen.umapyoi.events.handler.ClientSetupEvents;
import net.tracen.umapyoi.item.AbstractSuitItem;
import net.tracen.umapyoi.item.CreativeModeTabFiller;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.UmaSoulItem;
import net.tracen.umapyoi.network.OpenScreenPacket;
import net.tracen.umapyoi.registry.RegistryObject;

public class UmapyoiClient implements ClientModInitializer {
    private static final CreativeModeTab GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ItemRegistry.HACHIMI_MID.get()))
            .title(Component.translatable("itemGroup.umapyoi"))
            .build();

    private static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            new ResourceLocation(Umapyoi.MODID, "umapyoi")
    );

    @Override
    public void onInitializeClient() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, GROUP);
        ItemGroupEvents.modifyEntriesEvent(TAB_KEY).register(entries -> {
            for (RegistryObject<Item> object : ItemRegistry.ITEMS.getEntries()) {
                Item item = object.get();
                if (item instanceof CreativeModeTabFiller filler)
                    filler.fillItemCategory(entries);
                else
                    entries.accept(item);
            }
        });

        // ClientEvents
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

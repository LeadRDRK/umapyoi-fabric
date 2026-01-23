package net.tracen.umapyoi;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.item.CreativeModeTabFiller;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.LazyRegistrar;
import net.tracen.umapyoi.registry.RegistryObject;

public class UmapyoiCreativeGroup {
    public static final LazyRegistrar<CreativeModeTab> CREATIVE_MODE_TABS = LazyRegistrar
            .create(Registries.CREATIVE_MODE_TAB, Umapyoi.MODID);

    public static final RegistryObject<CreativeModeTab> UMAPYOI_ITEMS = CREATIVE_MODE_TABS.register("umapyoi",
            () -> FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ItemRegistry.HACHIMI_MID.get()))
                    .title(Component.translatable("itemGroup.umapyoi"))
                    .build());

    public static final RegistryObject<CreativeModeTab> UMAPYOI_BLANK_SOULS = CREATIVE_MODE_TABS.register("umapyoi_blank_souls",
            () -> FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ItemRegistry.BLANK_UMA_SOUL.get()))
                    .title(Component.translatable("itemGroup.umapyoi.blank_souls"))
                    .build());

    public static final RegistryObject<CreativeModeTab> UMAPYOI_SOULS = CREATIVE_MODE_TABS.register("umapyoi_souls",
            () -> FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ItemRegistry.UMA_SOUL_DISPLAY.get()))
                    .title(Component.translatable("itemGroup.umapyoi.souls"))
                    .build());

    public static final RegistryObject<CreativeModeTab> UMAPYOI_CARDS = CREATIVE_MODE_TABS.register("umapyoi_cards",
            () -> FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ItemRegistry.SUPPORT_CARD.get()))
                    .title(Component.translatable("itemGroup.umapyoi.cards"))
                    .build());

    public static final RegistryObject<CreativeModeTab> RACE_TICKETS = CREATIVE_MODE_TABS.register("race_tickets",
            () -> FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ItemRegistry.UMA_RACE_TICKET.get()))
                    .title(Component.translatable("itemGroup.umapyoi.race_tickets"))
                    .build());

    public static void register() {
        CREATIVE_MODE_TABS.register();

        ItemGroupEvents
                .modifyEntriesEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, UMAPYOI_ITEMS.getId()))
                .register(entries -> {
                    for (RegistryObject<Item> object : ItemRegistry.ITEMS.getEntries()) {
                        if (object == ItemRegistry.BLANK_UMA_SOUL || object == ItemRegistry.UMA_SOUL
                                || object == ItemRegistry.UMA_SOUL_DISPLAY || object == ItemRegistry.SUPPORT_CARD
                                || object == ItemRegistry.UMA_RACE_TICKET)
                            continue;

                        Item item = object.get();

                        if (item instanceof CreativeModeTabFiller filler)
                            filler.fillItemCategory(entries);
                        else
                            entries.accept(item);
                    }
                });

        ItemGroupEvents
                .modifyEntriesEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, UMAPYOI_BLANK_SOULS.getId()))
                .register(entries -> {
                    ((CreativeModeTabFiller) ItemRegistry.BLANK_UMA_SOUL.get()).fillItemCategory(entries);
                });

        ItemGroupEvents
                .modifyEntriesEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, UMAPYOI_SOULS.getId()))
                .register(entries -> {
                    ((CreativeModeTabFiller) ItemRegistry.UMA_SOUL.get()).fillItemCategory(entries);
                });

        ItemGroupEvents
                .modifyEntriesEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, UMAPYOI_CARDS.getId()))
                .register(entries -> {
                    ((CreativeModeTabFiller) ItemRegistry.SUPPORT_CARD.get()).fillItemCategory(entries);
                });

        ItemGroupEvents
                .modifyEntriesEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, RACE_TICKETS.getId()))
                .register(entries -> {
                    ((CreativeModeTabFiller) ItemRegistry.UMA_RACE_TICKET.get()).fillItemCategory(entries);
                });
    }
}

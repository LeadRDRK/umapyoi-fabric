package net.tracen.umapyoi;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
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
            () -> FabricCreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemRegistry.HACHIMI_MID))
                    .title(Component.translatable("itemGroup.umapyoi"))
                    .build());

    public static final RegistryObject<CreativeModeTab> UMAPYOI_BLANK_SOULS = CREATIVE_MODE_TABS.register("umapyoi_blank_souls",
            () -> FabricCreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemRegistry.BLANK_UMA_SOUL))
                    .title(Component.translatable("itemGroup.umapyoi.blank_souls"))
                    .build());

    public static final RegistryObject<CreativeModeTab> UMAPYOI_SOULS = CREATIVE_MODE_TABS.register("umapyoi_souls",
            () -> FabricCreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemRegistry.UMA_SOUL_DISPLAY))
                    .title(Component.translatable("itemGroup.umapyoi.souls"))
                    .build());

    public static final RegistryObject<CreativeModeTab> UMAPYOI_CARDS = CREATIVE_MODE_TABS.register("umapyoi_cards",
            () -> FabricCreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemRegistry.SUPPORT_CARD))
                    .title(Component.translatable("itemGroup.umapyoi.cards"))
                    .build());

    public static final RegistryObject<CreativeModeTab> UMAPYOI_RACETICKETS = CREATIVE_MODE_TABS.register("umapyoi_racetickets",
            () -> FabricCreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemRegistry.UMA_RACE_TICKET))
                    .title(Component.translatable("itemGroup.umapyoi.race_tickets"))
                    .build());

    public static final RegistryObject<CreativeModeTab> UMAPYOI_FACTORSHARDS = CREATIVE_MODE_TABS.register("umapyoi_factorshards",
            () -> FabricCreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemRegistry.FACTOR_SHARD))
                    .title(Component.translatable("itemGroup.umapyoi.factor_shards"))
                    .build());

    public static void register() {
        CREATIVE_MODE_TABS.register();

        CreativeModeTabEvents
                .modifyOutputEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, UMAPYOI_ITEMS.getId()))
                .register(entries -> {
                    for (Item item : ItemRegistry.ITEMS) {
                        if (item == ItemRegistry.BLANK_UMA_SOUL || item == ItemRegistry.UMA_SOUL
                                || item == ItemRegistry.UMA_SOUL_DISPLAY || item == ItemRegistry.SUPPORT_CARD
                                || item == ItemRegistry.UMA_RACE_TICKET
                                || item == ItemRegistry.MANUAL_CLOSED)
                            continue;

                        if (item instanceof CreativeModeTabFiller filler)
                            filler.fillItemCategory(entries);
                        else
                            entries.accept(item);
                    }
                });

        CreativeModeTabEvents
                .modifyOutputEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, UMAPYOI_BLANK_SOULS.getId()))
                .register(entries -> {
                    ((CreativeModeTabFiller) ItemRegistry.BLANK_UMA_SOUL).fillItemCategory(entries);
                });

        CreativeModeTabEvents
                .modifyOutputEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, UMAPYOI_SOULS.getId()))
                .register(entries -> {
                    ((CreativeModeTabFiller) ItemRegistry.UMA_SOUL).fillItemCategory(entries);
                });

        CreativeModeTabEvents
                .modifyOutputEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, UMAPYOI_CARDS.getId()))
                .register(entries -> {
                    ((CreativeModeTabFiller) ItemRegistry.SUPPORT_CARD).fillItemCategory(entries);
                });

        CreativeModeTabEvents
                .modifyOutputEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, UMAPYOI_RACETICKETS.getId()))
                .register(entries -> {
                    ((CreativeModeTabFiller) ItemRegistry.UMA_RACE_TICKET).fillItemCategory(entries);
                });

        CreativeModeTabEvents
                .modifyOutputEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, UMAPYOI_FACTORSHARDS.getId()))
                .register(entries -> {
                    ((CreativeModeTabFiller) ItemRegistry.FACTOR_SHARD).fillItemCategory(entries);
                });
    }
}

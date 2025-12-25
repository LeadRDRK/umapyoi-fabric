package net.tracen.umapyoi.data.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.tracen.umapyoi.item.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class UmapyoiItemTagsProvider extends ItemTagProvider {
    public UmapyoiItemTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        getOrCreateTagBuilder(UmapyoiItemTags.COMMON_GACHA_ITEM)
                .add(ItemRegistry.JEWEL.get()).add(ItemRegistry.BLANK_TICKET.get());
        getOrCreateTagBuilder(UmapyoiItemTags.SR_UMA_TICKET).add(ItemRegistry.SR_UMA_TICKET.get());
        getOrCreateTagBuilder(UmapyoiItemTags.SSR_UMA_TICKET).add(ItemRegistry.SSR_UMA_TICKET.get());
        getOrCreateTagBuilder(UmapyoiItemTags.SR_CARD_TICKET).add(ItemRegistry.SR_CARD_TICKET.get());
        getOrCreateTagBuilder(UmapyoiItemTags.SSR_CARD_TICKET).add(ItemRegistry.SSR_CARD_TICKET.get());
        getOrCreateTagBuilder(UmapyoiItemTags.WATER).add(Items.WATER_BUCKET);
        getOrCreateTagBuilder(UmapyoiItemTags.SUGAR).add(Items.SUGAR);
        getOrCreateTagBuilder(UmapyoiItemTags.MILK).add(Items.MILK_BUCKET);

        getOrCreateTagBuilder(UmapyoiItemTags.BREAD).addTag(UmapyoiItemTags.BREAD_WHEAT);
        getOrCreateTagBuilder(UmapyoiItemTags.BREAD_WHEAT).add(Items.BREAD);

        getOrCreateTagBuilder(UmapyoiItemTags.UMA_TICKET).addTag(UmapyoiItemTags.COMMON_GACHA_ITEM)
                .add(ItemRegistry.UMA_TICKET.get())
                .addTag(UmapyoiItemTags.SR_UMA_TICKET).addTag(UmapyoiItemTags.SSR_UMA_TICKET);
        getOrCreateTagBuilder(UmapyoiItemTags.CARD_TICKET).addTag(UmapyoiItemTags.COMMON_GACHA_ITEM)
                .add(ItemRegistry.CARD_TICKET.get())
                .addTag(UmapyoiItemTags.SR_CARD_TICKET).addTag(UmapyoiItemTags.SSR_CARD_TICKET);

        getOrCreateTagBuilder(UmapyoiItemTags.HORSESHOE).add(ItemRegistry.HORSESHOE_GOLD.get())
            .add(ItemRegistry.HORSESHOE_SILVER.get()).add(ItemRegistry.HORSESHOE_RAINBOW.get());
    }

}

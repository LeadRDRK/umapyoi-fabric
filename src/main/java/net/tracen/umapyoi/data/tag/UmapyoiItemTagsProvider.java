package net.tracen.umapyoi.data.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.tracen.umapyoi.item.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class UmapyoiItemTagsProvider extends ItemTagProvider {
    public UmapyoiItemTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        valueLookupBuilder(UmapyoiItemTags.SHOULD_RENDER).add(Items.ELYTRA);
        builder(UmapyoiItemTags.SHOULD_RENDER)
                .addOptional(ResourceKey.create(Registries.ITEM, Identifier.parse("corn_delight:cob_pipe")))
                .addOptional(ResourceKey.create(Registries.ITEM, Identifier.parse("create:goggles")));

        valueLookupBuilder(UmapyoiItemTags.COMMON_GACHA_ITEM)
                .add(ItemRegistry.JEWEL).add(ItemRegistry.BLANK_TICKET);
        valueLookupBuilder(UmapyoiItemTags.SR_UMA_TICKET).add(ItemRegistry.SR_UMA_TICKET);
        valueLookupBuilder(UmapyoiItemTags.SSR_UMA_TICKET).add(ItemRegistry.SSR_UMA_TICKET);
        valueLookupBuilder(UmapyoiItemTags.SR_CARD_TICKET).add(ItemRegistry.SR_CARD_TICKET);
        valueLookupBuilder(UmapyoiItemTags.SSR_CARD_TICKET).add(ItemRegistry.SSR_CARD_TICKET);

        valueLookupBuilder(UmapyoiItemTags.SUGAR).add(Items.SUGAR);
        valueLookupBuilder(UmapyoiItemTags.BAMBOO).add(Items.BAMBOO);

        valueLookupBuilder(UmapyoiItemTags.UMA_TICKET).addTag(UmapyoiItemTags.COMMON_GACHA_ITEM)
                .add(ItemRegistry.UMA_TICKET)
                .addTag(UmapyoiItemTags.SR_UMA_TICKET).addTag(UmapyoiItemTags.SSR_UMA_TICKET);
        valueLookupBuilder(UmapyoiItemTags.CARD_TICKET).addTag(UmapyoiItemTags.COMMON_GACHA_ITEM)
                .add(ItemRegistry.CARD_TICKET)
                .addTag(UmapyoiItemTags.SR_CARD_TICKET).addTag(UmapyoiItemTags.SSR_CARD_TICKET);

        valueLookupBuilder(UmapyoiItemTags.HORSESHOE).add(ItemRegistry.HORSESHOE_GOLD)
            .add(ItemRegistry.HORSESHOE_SILVER).add(ItemRegistry.HORSESHOE_RAINBOW);
        valueLookupBuilder(UmapyoiItemTags.HORSESHOE_RAINBOW).add(ItemRegistry.HORSESHOE_RAINBOW);
    }

}

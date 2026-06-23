package net.tracen.umapyoi.data.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider.ItemTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.utils.RaceRanking;

import java.util.concurrent.CompletableFuture;

public class UmapyoiItemTagsProvider extends ItemTagsProvider {
    public UmapyoiItemTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        builder(UmapyoiItemTags.SHOULD_RENDER).add(item(Items.ELYTRA));
        builder(UmapyoiItemTags.SHOULD_RENDER)
                .addOptional(ResourceKey.create(Registries.ITEM, Identifier.parse("corn_delight:cob_pipe")))
                .addOptional(ResourceKey.create(Registries.ITEM, Identifier.parse("create:goggles")));

        builder(UmapyoiItemTags.COMMON_GACHA_ITEM)
                .add(item(ItemRegistry.JEWEL)).add(item(ItemRegistry.BLANK_TICKET));
        builder(UmapyoiItemTags.SR_UMA_TICKET).add(item(ItemRegistry.SR_UMA_TICKET));
        builder(UmapyoiItemTags.SSR_UMA_TICKET).add(item(ItemRegistry.SSR_UMA_TICKET));
        builder(UmapyoiItemTags.SR_CARD_TICKET).add(item(ItemRegistry.SR_CARD_TICKET));
        builder(UmapyoiItemTags.SSR_CARD_TICKET).add(item(ItemRegistry.SSR_CARD_TICKET));

        builder(UmapyoiItemTags.SUGAR).add(item(Items.SUGAR));
        builder(UmapyoiItemTags.BAMBOO).add(item(Items.BAMBOO));

        builder(UmapyoiItemTags.UMA_TICKET).addTag(UmapyoiItemTags.COMMON_GACHA_ITEM)
                .add(item(ItemRegistry.UMA_TICKET))
                .addTag(UmapyoiItemTags.SR_UMA_TICKET).addTag(UmapyoiItemTags.SSR_UMA_TICKET);
        builder(UmapyoiItemTags.CARD_TICKET).addTag(UmapyoiItemTags.COMMON_GACHA_ITEM)
                .add(item(ItemRegistry.CARD_TICKET))
                .addTag(UmapyoiItemTags.SR_CARD_TICKET).addTag(UmapyoiItemTags.SSR_CARD_TICKET);

        builder(UmapyoiItemTags.HORSESHOE).add(item(ItemRegistry.HORSESHOE_GOLD))
            .add(item(ItemRegistry.HORSESHOE_SILVER)).add(item(ItemRegistry.HORSESHOE_RAINBOW));
        builder(UmapyoiItemTags.HORSESHOE_RAINBOW).add(item(ItemRegistry.HORSESHOE_RAINBOW));

        builder(UmapyoiItemTags.getMotivationFoodTag(1))
                .add(item(ItemRegistry.HACHIMI_MID))
                .add(item(ItemRegistry.CUPCAKE));

        builder(UmapyoiItemTags.getMotivationFoodTag(2))
                .add(item(ItemRegistry.HACHIMI_BIG))
                .add(item(ItemRegistry.SWEET_CUPCAKE));

        builder(UmapyoiItemTags.getMotivationFoodTag(-1))
                .add(item(ItemRegistry.ROYAL_BITTER));

        builder(UmapyoiItemTags.SLOW_METABOLISM)
                .add(item(ItemRegistry.HACHIMI_BIG)).add(item(ItemRegistry.SWEET_CUPCAKE));

        builder(UmapyoiItemTags.getRaceMaterialTag(RaceRanking.PREOP))
                .forceAddTag(ConventionalItemTags.COPPER_INGOTS);

        builder(UmapyoiItemTags.getRaceMaterialTag(RaceRanking.OP))
                .forceAddTag(ConventionalItemTags.IRON_INGOTS);

        builder(UmapyoiItemTags.getRaceMaterialTag(RaceRanking.GIII))
                .forceAddTag(ConventionalItemTags.GOLD_INGOTS);

        builder(UmapyoiItemTags.getRaceMaterialTag(RaceRanking.GII))
                .forceAddTag(ConventionalItemTags.EMERALD_GEMS);

        builder(UmapyoiItemTags.getRaceMaterialTag(RaceRanking.GI))
                .forceAddTag(ConventionalItemTags.DIAMOND_GEMS);

        builder(UmapyoiItemTags.RACE_CHAMPIONS_MATERIAL).add(item(Items.ENDER_EYE));
    }

    private ResourceKey<Item> item(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow();
    }
}

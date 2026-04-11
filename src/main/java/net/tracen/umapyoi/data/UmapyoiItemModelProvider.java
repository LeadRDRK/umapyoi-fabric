package net.tracen.umapyoi.data;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.utils.GachaRanking;
import net.tracen.umapyoi.utils.RaceRanking;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

public class UmapyoiItemModelProvider {
    private static final Set<Item> CUSTOM_MODEL_ITEMS = Set.of(
            ItemRegistry.BASEBALL_BAT,
            ItemRegistry.HACHIMI_BIG,
            ItemRegistry.HACHIMI_MID,
            ItemRegistry.NAGINATA,
            ItemRegistry.UMA_SOUL_DISPLAY,
            ItemRegistry.GATE,
            ItemRegistry.GATE_DOOR
    );

    private static final Set<Item> BLOCK_ITEMS_WITH_FLAT_MODEL = Set.of(
            ItemRegistry.THREE_GODDESS,
            ItemRegistry.UMA_STATUE
    );

    private static final Set<Item> EXCLUDE_ITEMS = Set.of(
    );

    private final ItemModelGenerators generator;

    public UmapyoiItemModelProvider(ItemModelGenerators generator) {
        this.generator = generator;
    }

    public void registerModels() {
        for (Item item : ItemRegistry.ITEMS) {
            if (EXCLUDE_ITEMS.contains(item) || registerDynamicModels(item))
                continue;

            if (CUSTOM_MODEL_ITEMS.contains(item)) {
                declareCustomModelItem(item);
            }
            else if (!(item instanceof BlockItem) || BLOCK_ITEMS_WITH_FLAT_MODEL.contains(item)) {
                generator.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
            }
        }
    }

    private void declareCustomModelItem(Item item) {
        generator.declareCustomModelItem(item);
    }

    private boolean registerDynamicModels(Item item) {
        if (item == ItemRegistry.SUPPORT_CARD) {
            String basePath = BuiltInRegistries.ITEM.getKey(item).getPath();
            var defaultModel = ModelTemplates.FLAT_ITEM.create(
                    ModelLocationUtils.getModelLocation(item),
                    TextureMapping.layer0(Umapyoi.id("item/" + basePath + "_ssr")),
                    generator.modelOutput
            );
            generator.itemModelOutput.accept(item, ItemModelUtils.plainModel(defaultModel));

            for (GachaRanking rank: GachaRanking.values()) {
                String path = basePath + "_" + rank.name().toLowerCase();
                String[] sep = path.split("/");
                StringWriter builder = new StringWriter();
                builder.write("item/");
                for (int i = 0; i < sep.length - 1; i++){
                    builder.write(sep[i]);
                    builder.write('/');
                }
                builder.write("support_card/");
                builder.write(sep[sep.length - 1]);
                String finalPath = builder.toString();
                ModelTemplates.FLAT_ITEM.create(
                        Umapyoi.id(finalPath),
                        TextureMapping.layer0(Umapyoi.id("item/" + path)),
                        generator.modelOutput
                );
            }
            return true;
        }

        if (item == ItemRegistry.UMA_RACE_TICKET) {
            String basePath = BuiltInRegistries.ITEM.getKey(item).getPath();
            var defaultModel = ModelTemplates.FLAT_ITEM.create(
                    ModelLocationUtils.getModelLocation(item),
                    TextureMapping.layer0(Umapyoi.id("item/" + basePath + "_common")),
                    generator.modelOutput
            );
            generator.itemModelOutput.accept(item, ItemModelUtils.plainModel(defaultModel));

            Stream.concat(
                    Arrays.stream(RaceRanking.values()).map(r -> r.textureSuffix),
                    Stream.of("champions")
            ).distinct().forEachOrdered((suffix) -> {
                String path = basePath + "_" + suffix;
                String[] sep = path.split("/");
                StringWriter builder = new StringWriter();
                builder.write("item/");
                for (int i = 0; i < sep.length - 1; i++){
                    builder.write(sep[i]);
                    builder.write('/');
                }
                builder.write("race_ticket/");
                builder.write(sep[sep.length - 1]);
                String finalPath = builder.toString();
                ModelTemplates.FLAT_ITEM.create(
                        Umapyoi.id(finalPath),
                        TextureMapping.layer0(Umapyoi.id("item/" + path)),
                        generator.modelOutput
                );
            });
            return true;
        }

        return false;
    }

    private ResourceLocation mcLoc(String name) {
        return ResourceLocation.withDefaultNamespace(name);
    }

    public static class FromBlockModels {
        private final BlockModelGenerators generator;

        public FromBlockModels(BlockModelGenerators generator) {
            this.generator = generator;
        }

        public void registerModels() {
            for (Item item : ItemRegistry.ITEMS) {
                if (EXCLUDE_ITEMS.contains(item)) continue;

                if (item instanceof BlockItem blockItem && !BLOCK_ITEMS_WITH_FLAT_MODEL.contains(item)
                        && !CUSTOM_MODEL_ITEMS.contains(item)) {
                    // the pedestal blocks's model locations are distinct from their block id
                    String modelName;
                    if (item == ItemRegistry.UMA_PEDESTAL) {
                        modelName = "pedestal";
                    }
                    else if (item == ItemRegistry.SILVER_UMA_PEDESTAL) {
                        modelName = "silver_pedestal";
                    }
                    else {
                        modelName = BuiltInRegistries.BLOCK.getKey(blockItem.getBlock()).getPath();
                    }

                    generator.registerSimpleItemModel(item, blockModel(modelName));
                }
            }
        }

        private static ResourceLocation blockModel(String name) {
            return Umapyoi.id("block/" + name);
        }
    }
}

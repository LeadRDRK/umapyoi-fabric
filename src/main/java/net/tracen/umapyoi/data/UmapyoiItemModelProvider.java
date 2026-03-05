package net.tracen.umapyoi.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.RegistryObject;
import net.tracen.umapyoi.utils.GachaRanking;
import net.tracen.umapyoi.utils.RaceRanking;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

public class UmapyoiItemModelProvider {
    private static final Set<RegistryObject<Item>> CUSTOM_MODEL_ITEMS = Set.of(
            ItemRegistry.BASEBALL_BAT,
            ItemRegistry.HACHIMI_BIG,
            ItemRegistry.HACHIMI_MID,
            ItemRegistry.NAGINATA,
            ItemRegistry.UMA_SOUL_DISPLAY
    );

    private static final Set<RegistryObject<Item>> BLOCK_ITEMS_WITH_FLAT_MODEL = Set.of(
            ItemRegistry.THREE_GODDESS,
            ItemRegistry.UMA_STATUE
    );

    private static final Set<RegistryObject<Item>> EXCLUDE_ITEMS = Set.of(
            ItemRegistry.GATE,
            ItemRegistry.GATE_DOOR
    );

    private final ItemModelGenerators generator;

    public UmapyoiItemModelProvider(ItemModelGenerators generator) {
        this.generator = generator;
    }

    public void registerModels() {
        for (RegistryObject<Item> entry : ItemRegistry.ITEMS.getEntries()) {
            if (registerDynamicModels(entry))
                continue;

            Item item = entry.get();
            if (EXCLUDE_ITEMS.contains(entry)
                    || (item instanceof BlockItem && !BLOCK_ITEMS_WITH_FLAT_MODEL.contains(entry)))
                continue;

            if (CUSTOM_MODEL_ITEMS.contains(entry)) {
                declareCustomModelItem(item);
            }
            else {
                generator.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
            }
        }
    }

    private void declareCustomModelItem(Item item) {
        // STUB: not needed until 1.21.5+
    }

    private boolean registerDynamicModels(RegistryObject<Item> item) {
        if (item == ItemRegistry.SUPPORT_CARD) {
            String basePath = item.getId().getPath();
            ModelTemplates.FLAT_ITEM.create(
                    ModelLocationUtils.getModelLocation(item.get()),
                    TextureMapping.layer0(Umapyoi.id("item/" + basePath + "_ssr")),
                    generator.output
            );
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
                        generator.output
                );
            }
            return true;
        }

        if (item == ItemRegistry.UMA_RACE_TICKET) {
            String basePath = item.getId().getPath();
            ModelTemplates.FLAT_ITEM.create(
                    ModelLocationUtils.getModelLocation(item.get()),
                    TextureMapping.layer0(Umapyoi.id("item/" + basePath + "_common")),
                    generator.output
            );

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
                        generator.output
                );
            });
            return true;
        }

        return false;
    }

    private ResourceLocation mcLoc(String name) {
        return new ResourceLocation(name);
    }

    public static class FromBlockModels {
        private final BlockModelGenerators generator;

        public FromBlockModels(BlockModelGenerators generator) {
            this.generator = generator;
        }

        public void registerModels() {
            for (RegistryObject<Item> entry : ItemRegistry.ITEMS.getEntries()) {
                if (EXCLUDE_ITEMS.contains(entry)) continue;

                Item item = entry.get();
                if (item instanceof BlockItem blockItem && !BLOCK_ITEMS_WITH_FLAT_MODEL.contains(entry)) {
                    // the pedestal blocks's model locations are distinct from their block id
                    String modelName;
                    if (item == ItemRegistry.UMA_PEDESTAL.get()) {
                        modelName = "pedestal";
                    }
                    else if (item == ItemRegistry.SILVER_UMA_PEDESTAL.get()) {
                        modelName = "silver_pedestal";
                    }
                    else {
                        modelName = BuiltInRegistries.BLOCK.getKey(blockItem.getBlock()).getPath();
                    }

                    generator.delegateItemModel(item, blockModel(modelName));
                }
            }

            for (RegistryObject<Item> entry : EXCLUDE_ITEMS) {
                if (entry.get() instanceof BlockItem blockItem)
                    generator.skipAutoItemBlock(blockItem.getBlock());
            }
        }

        private static ResourceLocation blockModel(String name) {
            return Umapyoi.id("block/" + name);
        }
    }
}

package net.tracen.umapyoi.data;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.ItemRegistry;

import java.util.Set;

public class UmapyoiModelProvider extends FabricModelProvider {
    private static final Set<Item> customModelItems = Set.of(
            ItemRegistry.BASEBALL_BAT,
            ItemRegistry.HACHIMI_BIG,
            ItemRegistry.HACHIMI_MID,
            ItemRegistry.NAGINATA,
            ItemRegistry.UMA_SOUL_DISPLAY,
            ItemRegistry.MANUAL_CLOSED
    );

    private static final Set<Item> blockItemsWithFlatModel = Set.of(
            ItemRegistry.THREE_GODDESS,
            ItemRegistry.UMA_STATUE
    );

    public UmapyoiModelProvider(FabricDataOutput output) {
        super(output);
    }

    private static Identifier blockModel(String name) {
        return Identifier.fromNamespaceAndPath(Umapyoi.MODID, "block/" + name);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        for (Item item : ItemRegistry.ITEMS) {
            if (item instanceof BlockItem blockItem && !blockItemsWithFlatModel.contains(item)) {
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

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        for (Item item : ItemRegistry.ITEMS) {
            if (item instanceof BlockItem && !blockItemsWithFlatModel.contains(item))
                continue;

            if (customModelItems.contains(item)) {
                generator.declareCustomModelItem(item);
            }
            else {
                generator.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
            }
        }
    }
}

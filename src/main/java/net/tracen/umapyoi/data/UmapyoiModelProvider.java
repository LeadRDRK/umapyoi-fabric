package net.tracen.umapyoi.data;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;

public class UmapyoiModelProvider extends FabricModelProvider {
    public UmapyoiModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        var blockStateProvider = new UmapyoiBlockStateProvider(generator);
        blockStateProvider.registerStatesAndModels();

        var blockItemModelProvider = new UmapyoiItemModelProvider.FromBlockModels(generator);
        blockItemModelProvider.registerModels();
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        var itemModelProvider = new UmapyoiItemModelProvider(generator);
        itemModelProvider.registerModels();
    }
}

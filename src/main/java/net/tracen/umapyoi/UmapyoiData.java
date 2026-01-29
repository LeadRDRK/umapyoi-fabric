package net.tracen.umapyoi;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.tracen.umapyoi.data.UmapyoiLootTableProvider;
import net.tracen.umapyoi.data.UmapyoiModelProvider;
import net.tracen.umapyoi.data.UmapyoiRecipeProvider;
import net.tracen.umapyoi.data.tag.UmapyoiBlockTagProvider;
import net.tracen.umapyoi.data.tag.UmapyoiItemTagsProvider;

public class UmapyoiData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(UmapyoiBlockTagProvider::new);
        pack.addProvider(UmapyoiItemTagsProvider::new);
        pack.addProvider(UmapyoiLootTableProvider::new);
        pack.addProvider(UmapyoiRecipeProvider::new);
        pack.addProvider(UmapyoiModelProvider::new);
    }
}

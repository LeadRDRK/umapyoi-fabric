package net.tracen.umapyoi;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.tracen.umapyoi.data.UmapyoiModelProvider;
import net.tracen.umapyoi.data.UmapyoiRecipeProvider;
import net.tracen.umapyoi.data.UmapyoiRegistryProvider;
import net.tracen.umapyoi.data.builtin.CostumeDataRegistry;
import net.tracen.umapyoi.data.builtin.SupportCardRegistry;
import net.tracen.umapyoi.data.builtin.UmaDataRegistry;
import net.tracen.umapyoi.data.loot.UmapyoiBlockLootTableProvider;
import net.tracen.umapyoi.data.loot.UmapyoiSkillLootTableProvider;
import net.tracen.umapyoi.data.tag.CosmeticDataTagProvider;
import net.tracen.umapyoi.data.tag.UmaDataTagProvider;
import net.tracen.umapyoi.data.tag.UmapyoiBlockTagProvider;
import net.tracen.umapyoi.data.tag.UmapyoiItemTagsProvider;
import net.tracen.umapyoi.data.tag.UmapyoiPOITagsProvider;
import net.tracen.umapyoi.registry.cosmetics.CosmeticData;
import net.tracen.umapyoi.registry.races.Race;
import net.tracen.umapyoi.registry.races.RaceRegistry;
import net.tracen.umapyoi.registry.races.field.RaceField;
import net.tracen.umapyoi.registry.races.field.RaceFieldRegistry;
import net.tracen.umapyoi.registry.races.tags.RaceTag;
import net.tracen.umapyoi.registry.races.tags.RaceTagRegistry;
import net.tracen.umapyoi.registry.training.card.SupportCard;
import net.tracen.umapyoi.registry.umadata.UmaData;

public class UmapyoiData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        // needed for custom registries in buildRegistry
        pack.addProvider(UmapyoiRegistryProvider::new);

        pack.addProvider(UmapyoiModelProvider::new);
        //pack.addProvider(UmapyoiSoundDefinitionProvider::new);
        pack.addProvider(UmapyoiBlockTagProvider::new);
        pack.addProvider(UmapyoiItemTagsProvider::new);
        pack.addProvider(UmapyoiBlockLootTableProvider::new);
        pack.addProvider(UmapyoiSkillLootTableProvider::new);
        pack.addProvider(UmaDataTagProvider::new);
        pack.addProvider(CosmeticDataTagProvider::new);
        pack.addProvider(UmapyoiPOITagsProvider::new);
        pack.addProvider(UmapyoiRecipeProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(UmaData.REGISTRY_KEY, UmaDataRegistry::registerAll);
        registryBuilder.add(SupportCard.REGISTRY_KEY, SupportCardRegistry::registerAll);
        registryBuilder.add(CosmeticData.REGISTRY_KEY, CostumeDataRegistry::registerAll);
        registryBuilder.add(Race.REGISTRY_KEY, RaceRegistry::registerAll);
        registryBuilder.add(RaceField.REGISTRY_KEY, RaceFieldRegistry::registerAll);
        registryBuilder.add(RaceTag.REGISTRY_KEY, RaceTagRegistry::registerAll);
    }
}

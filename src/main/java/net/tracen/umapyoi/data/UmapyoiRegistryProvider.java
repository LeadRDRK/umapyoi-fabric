package net.tracen.umapyoi.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.tracen.umapyoi.registry.cosmetics.CosmeticData;
import net.tracen.umapyoi.registry.races.Race;
import net.tracen.umapyoi.registry.races.field.RaceField;
import net.tracen.umapyoi.registry.races.tags.RaceTag;
import net.tracen.umapyoi.registry.training.card.SupportCard;
import net.tracen.umapyoi.registry.umadata.UmaData;

import java.util.concurrent.CompletableFuture;

public class UmapyoiRegistryProvider extends FabricDynamicRegistryProvider {
    public UmapyoiRegistryProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        var lookups = entries.getLookups();
        entries.addAll(lookups.lookupOrThrow(UmaData.REGISTRY_KEY));
        entries.addAll(lookups.lookupOrThrow(SupportCard.REGISTRY_KEY));
        entries.addAll(lookups.lookupOrThrow(CosmeticData.REGISTRY_KEY));
        entries.addAll(lookups.lookupOrThrow(Race.REGISTRY_KEY));
        entries.addAll(lookups.lookupOrThrow(RaceField.REGISTRY_KEY));
        entries.addAll(lookups.lookupOrThrow(RaceTag.REGISTRY_KEY));
    }

    @Override
    public String getName() {
        return "Umapyoi Registries";
    }
}

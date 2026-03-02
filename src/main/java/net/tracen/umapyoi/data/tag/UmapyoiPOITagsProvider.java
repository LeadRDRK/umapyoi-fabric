package net.tracen.umapyoi.data.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.PoiTypeTags;
import net.tracen.umapyoi.villager.VillageRegistry;

import java.util.concurrent.CompletableFuture;

public class UmapyoiPOITagsProvider extends PoiTypeTagsProvider {
    public UmapyoiPOITagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE)
                .add(ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, VillageRegistry.TRAINER_POI));
    }
}

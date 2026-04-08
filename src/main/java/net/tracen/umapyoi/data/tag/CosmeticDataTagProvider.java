package net.tracen.umapyoi.data.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.tracen.umapyoi.registry.cosmetics.CosmeticData;

import java.util.concurrent.CompletableFuture;

public class CosmeticDataTagProvider extends KeyTagProvider<CosmeticData> {
    public CosmeticDataTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, CosmeticData.REGISTRY_KEY, provider);
    }

    @Override
    public String getName() {
        return "Umamusume Costume Data Tag Provider";
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        this.tag(UmapyoiCostumeDataTags.HAT_HIDEHAIR);
    }

}

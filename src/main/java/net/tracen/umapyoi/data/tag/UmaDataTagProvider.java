package net.tracen.umapyoi.data.tag;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.tracen.umapyoi.data.builtin.UmaDataRegistry;
import net.tracen.umapyoi.registry.umadata.UmaData;

public class UmaDataTagProvider extends TagsProvider<UmaData> {
    public UmaDataTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, UmaData.REGISTRY_KEY, provider);
    }

    @Override
    public String getName() {
        return "Umamusume Data Tag Provider";
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        this.tag(UmapyoiUmaDataTags.FLAT_CHEST)
                .add(UmaDataRegistry.TOKAI_TEIO)
                .add(UmaDataRegistry.TAMAMO_CROSS)
                .add(UmaDataRegistry.MANHATTAN_CAFE)
                .add(UmaDataRegistry.SILENCE_SUZUKA)
                .add(UmaDataRegistry.TAMAMO_CROSS_FESTIVAL)
                .add(UmaDataRegistry.GRASS_WONDER)
                .add(UmaDataRegistry.MANHATTAN_CAFE_VALENTINE)
                .add(UmaDataRegistry.GRASS_WONDER_UMANET)
                .add(UmaDataRegistry.HARU_URARA)
                .add(UmaDataRegistry.FUJIMASA_MARCH)
                .add(UmaDataRegistry.AGNES_DIGITAL);
        this.tag(UmapyoiUmaDataTags.TANNED_SKIN).add(UmaDataRegistry.DARLEY_ARABIAN);
        this.tag(UmapyoiUmaDataTags.STUCK_MODEL);
        this.tag(UmapyoiUmaDataTags.ALTER_MODEL).add(UmaDataRegistry.AGNES_DIGITAL).add(UmaDataRegistry.TRANSCEND);
    }

}

package net.tracen.umapyoi.data.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.tracen.umapyoi.registry.UmaDataRegistry;
import net.tracen.umapyoi.registry.umadata.UmaData;

import java.util.concurrent.CompletableFuture;

public class UmaDataTagProvider extends FabricTagProvider<UmaData> {
    public UmaDataTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, UmaDataRegistry.UMA_DATA_REGISTRY.get().key(), completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        getOrCreateTagBuilder(UmapyoiUmaDataTags.FLAT_CHEST).add(UmaDataRegistry.TOKAI_TEIO.get())
                .add(UmaDataRegistry.TAMAMO_CROSS.get()).add(UmaDataRegistry.MANHATTAN_CAFE.get())
                .add(UmaDataRegistry.SILENCE_SUZUKA.get()).add(UmaDataRegistry.TAMAMO_CROSS_FESTIVAL.get())
                .add(UmaDataRegistry.GRASS_WONDER.get()).add(UmaDataRegistry.MEJIRO_MCQUEEN.get()).add(UmaDataRegistry.HARU_URARA.get());
        getOrCreateTagBuilder(UmapyoiUmaDataTags.TANNED_SKIN).add(UmaDataRegistry.DARLEY_ARABIAN.get());
    }

}

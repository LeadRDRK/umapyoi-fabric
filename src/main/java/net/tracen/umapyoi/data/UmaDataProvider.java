package net.tracen.umapyoi.data;

import com.google.common.collect.Maps;
import com.mojang.serialization.JsonOps;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.registry.UmaDataRegistry;
import net.tracen.umapyoi.registry.umadata.UmaData;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;

public class UmaDataProvider implements DataProvider {

    protected final FabricDataOutput output;
    protected final Map<ResourceLocation, UmaData> datas = Maps.newLinkedHashMap();
    protected final String modId;

    public UmaDataProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        this(output, Umapyoi.MODID);
    }

    public UmaDataProvider(FabricDataOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    public void addDatas() {
        for (Supplier<UmaData> data : UmaDataRegistry.UMA_DATA.getEntries()) {
            this.addData(data);
        }
    }

    public void addData(Supplier<UmaData> data) {
        this.datas.computeIfAbsent(data.get().getRegistryName(), loc -> data.get());
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return CompletableFuture.runAsync(() -> {
            this.datas.clear();
            this.addDatas();
            final Path outputFolder = output.getOutputFolder();
            this.datas.forEach((loc, data) -> {
                String pathString = String.join("/", PackType.SERVER_DATA.getDirectory(), loc.getNamespace(),
                        UmaData.REGISTRY_KEY.location().getNamespace(), UmaData.REGISTRY_KEY.location().getPath(),
                        loc.getPath() + ".json");
                Path path = outputFolder.resolve(pathString);

                UmaData.CODEC.encodeStart(JsonOps.INSTANCE, data)
                        .resultOrPartial(msg -> Umapyoi.getLogger().error("Failed to encode {}: {}", path, msg)) // Log
                        // error on
                        // encode
                        // failure.
                        .ifPresent(json -> // Output to file on encode success.
                        {
                            try {
                                DataProvider.saveStable(cachedOutput, json, path).join();
                            } catch (CompletionException e) {
                                Umapyoi.getLogger().error("Failed to save " + pathString, e);
                            }
                        });
            });
        });
    }

    @Override
    public String getName() {
        return String.format("%s provider for %s", "uma musume data", modId);
    }

}

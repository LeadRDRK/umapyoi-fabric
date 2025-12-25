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
import net.tracen.umapyoi.registry.SupportCardRegistry;
import net.tracen.umapyoi.registry.training.card.SupportCard;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;

public class SupportCardDataProvider implements DataProvider {

    protected final FabricDataOutput output;
    protected final Map<ResourceLocation, SupportCard> datas = Maps.newLinkedHashMap();
    protected final String modId;

    public SupportCardDataProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        this(output, Umapyoi.MODID);
    }

    public SupportCardDataProvider(FabricDataOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    public void addDatas() {
        for (Supplier<SupportCard> data : SupportCardRegistry.SUPPORT_CARD.getEntries()) {
            this.addData(data);
        }
    }


    public void addData(Supplier<SupportCard> data) {
        this.addData(data, data.get().getRegistryName());
    }
    
    public void addData(Supplier<SupportCard> data, ResourceLocation name) {
        this.datas.computeIfAbsent(name, loc -> data.get());
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return CompletableFuture.runAsync(() -> {
            this.datas.clear();
            this.addDatas();
            final Path outputFolder = output.getOutputFolder();
            this.datas.forEach((loc, data) -> {
                String pathString = String.join("/", PackType.SERVER_DATA.getDirectory(), loc.getNamespace(),
                        SupportCard.REGISTRY_KEY.location().getNamespace(), SupportCard.REGISTRY_KEY.location().getPath(),
                        loc.getPath() + ".json");
                Path path = outputFolder.resolve(pathString);

                SupportCard.CODEC.encodeStart(JsonOps.INSTANCE, data)
                        .resultOrPartial(msg -> Umapyoi.getLogger().error("Failed to encode {}: {}", path, msg)) // Log
                        // error on
                        // encode
                        // failure.
                        .ifPresent(json -> // Output to file on encode success.
                        {
                            try {
                                DataProvider.saveStable(cachedOutput, json, path).join();
                            }
                            catch (CompletionException e) {
                                Umapyoi.getLogger().error("Failed to save " + pathString, e);
                            }
                        });
            });
        });
    }

    @Override
    public String getName() {
        return String.format("%s provider for %s", "supplier card", modId);
    }

}

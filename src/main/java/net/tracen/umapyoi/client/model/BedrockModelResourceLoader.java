package net.tracen.umapyoi.client.model;

import com.google.gson.JsonElement;

import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.DataGenUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/** Ported from MMLib **/
public class BedrockModelResourceLoader implements SimpleResourceReloadListener<Map<ResourceLocation, JsonElement>> {
    private final String resource_path;
    public BedrockModelResourceLoader(String path) {
        this.resource_path = path;
    }

    @Override
    public CompletableFuture<Map<ResourceLocation, JsonElement>> load(ResourceManager manager, ProfilerFiller profiler, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            HashMap<ResourceLocation, JsonElement> map = new HashMap<ResourceLocation, JsonElement>();
            SimpleJsonResourceReloadListener.scanDirectory(manager, resource_path, DataGenUtils.DATA_GSON, map);
            return map;
        });
    }

    @Override
    public CompletableFuture<Void> apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler, Executor executor) {
        return CompletableFuture.runAsync(() -> {
            ClientUtils.MODEL_MAP.clear();
            Umapyoi.getLogger().info("Started Loading Bedrock Model from : {}", resource_path);
            if (map.isEmpty())
                Umapyoi.getLogger().error("{} is an empty folder!", resource_path);
            for (var entry : map.entrySet()) {
                Umapyoi.getLogger().info("Loading Bedrock Model Loading : {}", entry.getKey().toString());
                ClientUtils.loadModel(entry.getKey(), entry.getValue());
            }
        });
    }

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "bmrl");
    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }
}
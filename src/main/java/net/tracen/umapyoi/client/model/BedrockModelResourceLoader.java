package net.tracen.umapyoi.client.model;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;

import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.DataGenUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/** Ported from MMLib **/
public class BedrockModelResourceLoader implements SimpleResourceReloadListener<Map<Identifier, JsonElement>> {
    private final String resource_path;
    public BedrockModelResourceLoader(String path) {
        this.resource_path = path;
    }

    @Override
    public CompletableFuture<Map<Identifier, JsonElement>> load(ResourceManager manager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            HashMap<Identifier, JsonElement> map = new HashMap<Identifier, JsonElement>();
            SimpleJsonResourceReloadListener.scanDirectory(manager, FileToIdConverter.json(resource_path),
                    JsonOps.INSTANCE, DataGenUtils.JSON_ELEMENT_CODEC, map);
            return map;
        });
    }

    @Override
    public CompletableFuture<Void> apply(Map<Identifier, JsonElement> data, ResourceManager manager, Executor executor) {
        return CompletableFuture.runAsync(() -> {
            ClientUtils.MODEL_MAP.clear();
            Umapyoi.getLogger().info("Started Loading Bedrock Model from : {}", resource_path);
            if (data.isEmpty())
                Umapyoi.getLogger().error("{} is an empty folder!", resource_path);
            for (var entry : data.entrySet()) {
                Umapyoi.getLogger().info("Loading Bedrock Model Loading : {}", entry.getKey().toString());
                ClientUtils.loadModel(entry.getKey(), entry.getValue());
            }
        });
    }

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Umapyoi.MODID, "bmrl");
    @Override
    public Identifier getFabricId() {
        return ID;
    }
}
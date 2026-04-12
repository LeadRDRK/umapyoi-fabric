package net.tracen.umapyoi.data;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.AtlasProvider;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.CachedOutput;
import net.tracen.umapyoi.client.renderer.blockentity.SupportAlbumPedestalBlockRenderer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UmapyoiAtlasProvider extends AtlasProvider {
    public UmapyoiAtlasProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return CompletableFuture.allOf(
                storeAtlas(output, AtlasIds.BLOCKS, blockSprites())
        );
    }

    private List<SpriteSource> blockSprites() {
        return List.of(
                new SingleFile(SupportAlbumPedestalBlockRenderer.BOOK_TEXTURE.texture())
        );
    }
}

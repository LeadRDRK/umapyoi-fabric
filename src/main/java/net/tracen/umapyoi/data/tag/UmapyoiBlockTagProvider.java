package net.tracen.umapyoi.data.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.tracen.umapyoi.block.BlockRegistry;

import java.util.concurrent.CompletableFuture;

public class UmapyoiBlockTagProvider extends BlockTagProvider {
    public UmapyoiBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(BlockRegistry.THREE_GODDESS.get())
                .add(BlockRegistry.SUPPORT_ALBUM_PEDESTAL.get()).add(BlockRegistry.UMA_PEDESTAL.get())
                .add(BlockRegistry.SILVER_SUPPORT_ALBUM_PEDESTAL.get()).add(BlockRegistry.SILVER_UMA_PEDESTAL.get())
                .add(BlockRegistry.DISASSEMBLY_BLOCK.get())
                .add(BlockRegistry.THREE_GODDESS_UPPER.get()).add(BlockRegistry.TRAINING_FACILITY.get());

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE).add(BlockRegistry.SKILL_LEARNING_TABLE.get())
                .add(BlockRegistry.REGISTER_LECTERN.get());

        getOrCreateTagBuilder(UmapyoiBlockTags.TRACK_TURF)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.DIRT_PATH)
                .add(Blocks.CRIMSON_NYLIUM)
                .add(Blocks.WARPED_NYLIUM);

        getOrCreateTagBuilder(UmapyoiBlockTags.TRACK_DIRT)
                .add(Blocks.DIRT)
                .add(Blocks.PODZOL)
                .add(Blocks.ROOTED_DIRT)
                .add(Blocks.COARSE_DIRT)
                .forceAddTag(BlockTags.SAND);

        getOrCreateTagBuilder(UmapyoiBlockTags.TRACK_SNOW)
                .forceAddTag(BlockTags.SNOW);
    }
}

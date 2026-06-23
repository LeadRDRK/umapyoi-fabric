package net.tracen.umapyoi.data.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider.BlockTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.tracen.umapyoi.block.BlockRegistry;

import java.util.concurrent.CompletableFuture;

public class UmapyoiBlockTagProvider extends BlockTagsProvider {
    public UmapyoiBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        builder(BlockTags.MINEABLE_WITH_PICKAXE).add(block(BlockRegistry.THREE_GODDESS))
                .add(block(BlockRegistry.SUPPORT_ALBUM_PEDESTAL)).add(block(BlockRegistry.UMA_PEDESTAL))
                .add(block(BlockRegistry.SILVER_SUPPORT_ALBUM_PEDESTAL)).add(block(BlockRegistry.SILVER_UMA_PEDESTAL))
                .add(block(BlockRegistry.UMA_STATUES)).add(block(BlockRegistry.THREE_GODDESS_UPPER))
                .add(block(BlockRegistry.TRAINING_FACILITY)).add(block(BlockRegistry.FACTOR_DECOMPOSE_TABLE))
                .add(block(BlockRegistry.FACTOR_RESEARCH_TABLE)).add(block(BlockRegistry.GATE))
                .add(block(BlockRegistry.GATE_DOOR)).add(block(BlockRegistry.RACE_REGISTER_BLOCK));

        builder(BlockTags.MINEABLE_WITH_AXE).add(block(BlockRegistry.DISASSEMBLY_BLOCK)).add(block(BlockRegistry.SKILL_LEARNING_TABLE))
                .add(block(BlockRegistry.REGISTER_LECTERN)).add(block(BlockRegistry.UMA_SELECT_BLOCK));

        builder(UmapyoiBlockTags.TRACK_TURF)
                .add(block(Blocks.GRASS_BLOCK))
                .add(block(Blocks.DIRT_PATH))
                .add(block(Blocks.CRIMSON_NYLIUM))
                .add(block(Blocks.WARPED_NYLIUM));

        builder(UmapyoiBlockTags.TRACK_DIRT)
                .add(block(Blocks.DIRT))
                .add(block(Blocks.PODZOL))
                .add(block(Blocks.ROOTED_DIRT))
                .add(block(Blocks.COARSE_DIRT))
                .forceAddTag(BlockTags.SAND);

        builder(UmapyoiBlockTags.TRACK_SNOW)
                .forceAddTag(BlockTags.SNOW);

        builder(UmapyoiBlockTags.PEDESTAL_UMA)
                .add(block(BlockRegistry.UMA_PEDESTAL))
                .add(block(BlockRegistry.SILVER_UMA_PEDESTAL));

        builder(UmapyoiBlockTags.PEDESTAL_SUPPORT)
                .add(block(BlockRegistry.SUPPORT_ALBUM_PEDESTAL))
                .add(block(BlockRegistry.SILVER_SUPPORT_ALBUM_PEDESTAL));
    }

    private ResourceKey<Block> block(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
    }
}

package net.tracen.umapyoi.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.item.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class UmapyoiLootTableProvider extends FabricBlockLootTableProvider {
    public UmapyoiLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(BlockRegistry.THREE_GODDESS);
        dropSelf(BlockRegistry.REGISTER_LECTERN);
        dropSelf(BlockRegistry.SKILL_LEARNING_TABLE);
        dropSelf(BlockRegistry.TRAINING_FACILITY);
        dropSelf(BlockRegistry.UMA_PEDESTAL);
        dropSelf(BlockRegistry.SILVER_UMA_PEDESTAL);
        dropSelf(BlockRegistry.DISASSEMBLY_BLOCK);
        dropSelf(BlockRegistry.UMA_SELECT_BLOCK);
        dropSelf(BlockRegistry.UMA_STATUES);
        dropOther(BlockRegistry.SUPPORT_ALBUM_PEDESTAL, ItemRegistry.UMA_PEDESTAL);
        dropOther(BlockRegistry.SILVER_SUPPORT_ALBUM_PEDESTAL, ItemRegistry.SILVER_UMA_PEDESTAL);
    }
}

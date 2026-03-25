package net.tracen.umapyoi.data.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.utils.ThreeBlockPart;

import java.util.concurrent.CompletableFuture;

public class UmapyoiBlockLootTableProvider extends FabricBlockLootTableProvider {
    public UmapyoiBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
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
        dropSelf(BlockRegistry.FACTOR_DECOMPOSE_TABLE.get());
        dropSelf(BlockRegistry.FACTOR_RESEARCH_TABLE.get());
        dropSelf(BlockRegistry.GATE.get());
        dropSelf(BlockRegistry.GATE_DOOR.get());
        dropSelf(BlockRegistry.RACE_REGISTER_BLOCK.get());
        add(BlockRegistry.GATE.get(), block -> createSinglePropConditionTable(block, ThreeBlockPart.PART, ThreeBlockPart.LOWER));
    }
}

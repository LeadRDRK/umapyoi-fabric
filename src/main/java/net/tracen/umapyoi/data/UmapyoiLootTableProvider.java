package net.tracen.umapyoi.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.item.ItemRegistry;

public class UmapyoiLootTableProvider extends FabricBlockLootTableProvider {
    public UmapyoiLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        dropSelf(BlockRegistry.THREE_GODDESS.get());
        dropSelf(BlockRegistry.REGISTER_LECTERN.get());
        dropSelf(BlockRegistry.SKILL_LEARNING_TABLE.get());
        dropSelf(BlockRegistry.TRAINING_FACILITY.get());
        dropSelf(BlockRegistry.UMA_PEDESTAL.get());
        dropSelf(BlockRegistry.SILVER_UMA_PEDESTAL.get());
        dropSelf(BlockRegistry.DISASSEMBLY_BLOCK.get());
        dropOther(BlockRegistry.SUPPORT_ALBUM_PEDESTAL.get(), ItemRegistry.UMA_PEDESTAL.get());
        dropOther(BlockRegistry.SILVER_SUPPORT_ALBUM_PEDESTAL.get(), ItemRegistry.SILVER_UMA_PEDESTAL.get());
    }
}

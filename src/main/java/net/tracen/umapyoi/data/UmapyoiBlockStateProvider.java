package net.tracen.umapyoi.data;

import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.block.Gate;

public class UmapyoiBlockStateProvider {
    private final BlockModelGenerators generator;

    public UmapyoiBlockStateProvider(BlockModelGenerators generator) {
        this.generator = generator;
    }

    public void registerStatesAndModels() {
        horizontalBlock(BlockRegistry.THREE_GODDESS.get(), Umapyoi.id("block/three_goddess"));
        horizontalBlock(BlockRegistry.UMA_STATUES.get(), Umapyoi.id("block/uma_statue"));
        horizontalBlock(BlockRegistry.TRAINING_FACILITY.get(), Umapyoi.id("block/training_facility"));
        horizontalBlock(BlockRegistry.DISASSEMBLY_BLOCK.get(), Umapyoi.id("block/disassembly_block"));
        horizontalBlock(BlockRegistry.REGISTER_LECTERN.get(), Umapyoi.id("block/register_lectern"));
        horizontalBlock(BlockRegistry.UMA_SELECT_BLOCK.get(), Umapyoi.id("block/uma_select_block"));
        horizontalBlock(BlockRegistry.RACE_REGISTER_BLOCK.get(), Umapyoi.id("block/race_register"));

        registerGate();

        simpleBlock(BlockRegistry.SKILL_LEARNING_TABLE.get(), Umapyoi.id("block/skill_learning_table"));
        simpleBlock(BlockRegistry.UMA_PEDESTAL.get(), Umapyoi.id("block/pedestal"));
        simpleBlock(BlockRegistry.SUPPORT_ALBUM_PEDESTAL.get(), Umapyoi.id("block/pedestal"));
        simpleBlock(BlockRegistry.SILVER_UMA_PEDESTAL.get(), Umapyoi.id("block/silver_pedestal"));
        simpleBlock(BlockRegistry.SILVER_SUPPORT_ALBUM_PEDESTAL.get(), Umapyoi.id("block/silver_pedestal"));

        simpleBlock(BlockRegistry.THREE_GODDESS_UPPER.get(), Umapyoi.id("block/three_goddess"));
        simpleBlock(BlockRegistry.UMA_STATUES_UPPER.get(), Umapyoi.id("block/uma_statue"));
        simpleBlock(BlockRegistry.GATE_DOOR.get(), Umapyoi.id("block/gate_door"));
        horizontalBlock(BlockRegistry.FACTOR_RESEARCH_TABLE.get(), Umapyoi.id("block/factor_research_table"));
        horizontalBlock(BlockRegistry.FACTOR_DECOMPOSE_TABLE.get(), Umapyoi.id("block/factor_decompose_table"));
    }

    private void registerGate() {
        PropertyDispatch dispatch = PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, Gate.PART)
                .generate((facing, part) -> Variant.variant()
                        .with(VariantProperties.MODEL, Umapyoi.id("block/gate_" + part.name().toLowerCase()))
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.values()[((int) facing.toYRot() + 180) % 360 / 90]));

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(BlockRegistry.GATE.get())
                        .with(dispatch)
        );
    }

    private void simpleBlock(Block block, ResourceLocation modelLoc) {
        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block,
                        Variant.variant().with(VariantProperties.MODEL, modelLoc))
        );
    }

    private static final int DEFAULT_ANGLE_OFFSET = 180;

    private void horizontalBlock(Block block, ResourceLocation modelLoc, int rotation) {
        PropertyDispatch dispatch = PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
                .generate(facing -> Variant.variant()
                        .with(VariantProperties.MODEL, modelLoc)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.values()[((int) facing.toYRot() + rotation) % 360 / 90]));

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block).with(dispatch)
        );
    }

    private void horizontalBlock(Block block, ResourceLocation modelLoc) {
        horizontalBlock(block, modelLoc, DEFAULT_ANGLE_OFFSET);
    }
}

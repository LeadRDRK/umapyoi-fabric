package net.tracen.umapyoi.data;

import com.mojang.math.Quadrant;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
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
        horizontalBlock(BlockRegistry.THREE_GODDESS, Umapyoi.id("block/three_goddess"));
        horizontalBlock(BlockRegistry.UMA_STATUES, Umapyoi.id("block/uma_statue"));
        horizontalBlock(BlockRegistry.TRAINING_FACILITY, Umapyoi.id("block/training_facility"));
        horizontalBlock(BlockRegistry.DISASSEMBLY_BLOCK, Umapyoi.id("block/disassembly_block"));
        horizontalBlock(BlockRegistry.REGISTER_LECTERN, Umapyoi.id("block/register_lectern"));
        horizontalBlock(BlockRegistry.UMA_SELECT_BLOCK, Umapyoi.id("block/uma_select_block"));
        horizontalBlock(BlockRegistry.RACE_REGISTER_BLOCK, Umapyoi.id("block/race_register"));
        horizontalBlock(BlockRegistry.RACE_SELECT_BLOCK, Umapyoi.id("block/race_select_block"));

        registerGate();

        simpleBlock(BlockRegistry.SKILL_LEARNING_TABLE, Umapyoi.id("block/skill_learning_table"));
        simpleBlock(BlockRegistry.UMA_PEDESTAL, Umapyoi.id("block/pedestal"));
        simpleBlock(BlockRegistry.SUPPORT_ALBUM_PEDESTAL, Umapyoi.id("block/pedestal"));
        simpleBlock(BlockRegistry.SILVER_UMA_PEDESTAL, Umapyoi.id("block/silver_pedestal"));
        simpleBlock(BlockRegistry.SILVER_SUPPORT_ALBUM_PEDESTAL, Umapyoi.id("block/silver_pedestal"));

        simpleBlock(BlockRegistry.THREE_GODDESS_UPPER, Umapyoi.id("block/three_goddess"));
        simpleBlock(BlockRegistry.UMA_STATUES_UPPER, Umapyoi.id("block/uma_statue"));
        simpleBlock(BlockRegistry.GATE_DOOR, Umapyoi.id("block/gate_door"));
        horizontalBlock(BlockRegistry.FACTOR_RESEARCH_TABLE, Umapyoi.id("block/factor_research_table"));
        horizontalBlock(BlockRegistry.FACTOR_DECOMPOSE_TABLE, Umapyoi.id("block/factor_decompose_table"));
    }

    private void registerGate() {
        PropertyDispatch<MultiVariant> dispatch = PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING, Gate.PART)
                .generate((facing, part) -> new MultiVariant(WeightedList.of(
                        new Variant(Umapyoi.id("block/gate_" + part.name().toLowerCase()))
                                .withYRot(Quadrant.values()[((int) facing.toYRot() + 180) % 360 / 90]))));

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(BlockRegistry.GATE)
                        .with(dispatch)
        );
    }

    private void simpleBlock(Block block, Identifier modelLoc) {
        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block,
                        new MultiVariant(WeightedList.of(new Variant(modelLoc))))
        );
    }

    private static final int DEFAULT_ANGLE_OFFSET = 180;

    private void horizontalBlock(Block block, Identifier modelLoc, int rotation) {
        PropertyDispatch<MultiVariant> dispatch = PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING)
                .generate(facing -> new MultiVariant(WeightedList.of(
                        new Variant(modelLoc)
                                .withYRot(Quadrant.values()[((int) facing.toYRot() + rotation) % 360 / 90]))));

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block).with(dispatch)
        );
    }

    private void horizontalBlock(Block block, Identifier modelLoc) {
        horizontalBlock(block, modelLoc, DEFAULT_ANGLE_OFFSET);
    }
}

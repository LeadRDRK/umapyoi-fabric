package net.tracen.umapyoi.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.Shapes;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.registry.RegistryObject;

import java.util.function.Function;

public class BlockRegistry {
    public static final Block SILVER_UMA_PEDESTAL = register("silver_uma_pedestal",
            SilverUmaPedestalBlock::new,
            BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE).noOcclusion());

    public static final Block SILVER_SUPPORT_ALBUM_PEDESTAL = register("silver_support_album_pedestal",
            SilverSupportAlbumPedestalBlock::new,
            BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE).noOcclusion());

    public static final Block UMA_PEDESTAL = register("uma_pedestal",
            UmaPedestalBlock::new,
            BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE).noOcclusion());

    public static final Block SUPPORT_ALBUM_PEDESTAL = register("support_album_pedestal",
            SupportAlbumPedestalBlock::new,
            BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE).noOcclusion());

    public static final Block THREE_GODDESS = register("three_goddess",
            ThreeGoddessBlock::new,
            BlockBehaviour.Properties.ofLegacyCopy(Blocks.POLISHED_ANDESITE).noOcclusion());

    public static final Block THREE_GODDESS_UPPER = register("three_goddess_upper",
            p -> new StatuesUpperBlock(THREE_GODDESS, Shapes.block(), true, p),
            BlockBehaviour.Properties.ofLegacyCopy(Blocks.POLISHED_ANDESITE).noOcclusion());

    public static final Block TRAINING_FACILITY = register("training_facility",
            TrainingFacilityBlock::new,
            BlockBehaviour.Properties.ofLegacyCopy(Blocks.IRON_BLOCK).noOcclusion().noCollision());

    public static final Block SKILL_LEARNING_TABLE = register("skill_learning_table",
            SkillLearningTableBlock::new,
            BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_WOOD).noOcclusion());

    public static final Block REGISTER_LECTERN = register("register_lectern",
            RetireRegisterLecternBlock::new,
            BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_WOOD));
    
    public static final Block DISASSEMBLY_BLOCK = register("disassembly_block",
            DisassemblyBlock::new,
            BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_WOOD));
    
    public static final Block UMA_STATUES = register("uma_statues",
            UmaStatueBlock::new,
            BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE).noOcclusion());
    
    public static final Block UMA_STATUES_UPPER = register("uma_statues_upper",
            p -> new StatuesUpperBlock(UMA_STATUES, Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D), true, p),
            BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE).noOcclusion());

    public static final Block UMA_SELECT_BLOCK = register("uma_select_block",
            UmaSelectBlock::new,
            BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_WOOD));

    public static final RegistryObject<Block> RACE_SELECT_BLOCK = BLOCKS.register("race_select_block",
            RaceSelectBlock::new);

    public static final RegistryObject<Block> FACTOR_DECOMPOSE_TABLE = BLOCKS.register("factor_decompose_table",
            FactorDecomposeTable::new);

    public static final RegistryObject<Block> FACTOR_RESEARCH_TABLE = BLOCKS.register("factor_research_table",
            FactorResearchTableBlock::new);

    public static final RegistryObject<Block> RACE_REGISTER_BLOCK = BLOCKS.register("race_register",
            RaceRegisterBlock::new);

    public static final RegistryObject<Block> GATE_DOOR = BLOCKS.register("gate_door", GateDoor::new);

    public static final RegistryObject<Block> GATE = BLOCKS.register("gate", Gate::new);

    private static ResourceKey<Block> modBlockId(String name) {
        return ResourceKey.create(Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, name));
    }

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        return register(modBlockId(name), factory, properties);
    }

    public static Block register(ResourceKey<Block> resourceKey, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        Block block = factory.apply(properties.setId(resourceKey));
        return Registry.register(BuiltInRegistries.BLOCK, resourceKey, block);
    }
}

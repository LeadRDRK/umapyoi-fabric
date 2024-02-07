package net.tracen.umapyoi.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.registry.LazyRegistrar;
import net.tracen.umapyoi.registry.RegistryObject;

public class BlockRegistry {
    public static final LazyRegistrar<Block> BLOCKS = LazyRegistrar.create(Registries.BLOCK, Umapyoi.MODID);
    public static final RegistryObject<Block> SILVER_UMA_PEDESTAL = BLOCKS.register("silver_uma_pedestal", SilverUmaPedestalBlock::new);
    public static final RegistryObject<Block> SILVER_SUPPORT_ALBUM_PEDESTAL = BLOCKS.register("silver_support_album_pedestal",
            SilverSupportAlbumPedestalBlock::new);
    public static final RegistryObject<Block> UMA_PEDESTAL = BLOCKS.register("uma_pedestal", UmaPedestalBlock::new);
    public static final RegistryObject<Block> SUPPORT_ALBUM_PEDESTAL = BLOCKS.register("support_album_pedestal",
            SupportAlbumPedestalBlock::new);
    public static final RegistryObject<Block> THREE_GODDESS = BLOCKS.register("three_goddess", ThreeGoddessBlock::new);
    public static final RegistryObject<Block> THREE_GODDESS_UPPER = BLOCKS.register("three_goddess_upper",
            ()->new StatuesUpperBlock(THREE_GODDESS));
    public static final RegistryObject<Block> TRAINING_FACILITY = BLOCKS.register("training_facility",
            TrainingFacilityBlock::new);
    public static final RegistryObject<Block> SKILL_LEARNING_TABLE = BLOCKS.register("skill_learning_table",
            SkillLearningTableBlock::new);
    public static final RegistryObject<Block> REGISTER_LECTERN = BLOCKS.register("register_lectern",
            RetireRegisterLecternBlock::new);
    
    public static final RegistryObject<Block> DISASSEMBLY_BLOCK = BLOCKS.register("disassembly_block",
            DisassemblyBlock::new);
    
    public static final RegistryObject<Block> UMA_STATUES = BLOCKS.register("uma_statues",
            UmaStatueBlock::new);
    
    public static final RegistryObject<Block> UMA_STATUES_UPPER = BLOCKS.register("uma_statues_upper",
            ()->new StatuesUpperBlock(UMA_STATUES, Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D)));
}

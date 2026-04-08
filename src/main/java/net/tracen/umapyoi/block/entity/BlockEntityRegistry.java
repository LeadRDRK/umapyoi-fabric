package net.tracen.umapyoi.block.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.registry.LazyRegistrar;
import net.tracen.umapyoi.registry.RegistryObject;

import java.util.Set;

public class BlockEntityRegistry {
    public static final LazyRegistrar<BlockEntityType<?>> BLOCK_ENTITIES = LazyRegistrar
            .create(Registries.BLOCK_ENTITY_TYPE, Umapyoi.MODID);

    public static final RegistryObject<BlockEntityType<ThreeGoddessBlockEntity>> THREE_GODDESS = BLOCK_ENTITIES
            .register("three_goddess", () -> new BlockEntityType<>(ThreeGoddessBlockEntity::new, Set.of(BlockRegistry.THREE_GODDESS)));

    public static final RegistryObject<BlockEntityType<TrainingFacilityBlockEntity>> TRAINING_FACILITY = BLOCK_ENTITIES
            .register("training_facility", () -> new BlockEntityType<>(TrainingFacilityBlockEntity::new, Set.of(BlockRegistry.TRAINING_FACILITY)));

    public static final RegistryObject<BlockEntityType<UmaPedestalBlockEntity>> UMA_PEDESTAL = BLOCK_ENTITIES
            .register("uma_pedestal", () -> new BlockEntityType<>(UmaPedestalBlockEntity::new, Set.of(BlockRegistry.UMA_PEDESTAL)));

    public static final RegistryObject<BlockEntityType<SupportAlbumPedestalBlockEntity>> SUPPORT_ALBUM_PEDESTAL = BLOCK_ENTITIES
            .register("support_album_pedestal", () -> new BlockEntityType<>(SupportAlbumPedestalBlockEntity::new, Set.of(BlockRegistry.SUPPORT_ALBUM_PEDESTAL)));
    
    public static final RegistryObject<BlockEntityType<SilverUmaPedestalBlockEntity>> SILVER_UMA_PEDESTAL = BLOCK_ENTITIES
            .register("silver_uma_pedestal", () -> new BlockEntityType<>(SilverUmaPedestalBlockEntity::new, Set.of(BlockRegistry.SILVER_UMA_PEDESTAL)));

    public static final RegistryObject<BlockEntityType<SilverSupportAlbumPedestalBlockEntity>> SILVER_SUPPORT_ALBUM_PEDESTAL = BLOCK_ENTITIES
            .register("silver_support_album_pedestal", () -> new BlockEntityType<>(SilverSupportAlbumPedestalBlockEntity::new, Set.of(BlockRegistry.SILVER_SUPPORT_ALBUM_PEDESTAL)));
    
    public static final RegistryObject<BlockEntityType<UmaStatueBlockEntity>> UMA_STATUES = BLOCK_ENTITIES
            .register("uma_statues", () -> new BlockEntityType<>(UmaStatueBlockEntity::new, Set.of(BlockRegistry.UMA_STATUES)));

    public static final RegistryObject<BlockEntityType<RaceRegisterBlockEntity>> RACE_REGISTER_BLOCK_ENTITY = BLOCK_ENTITIES
            .register("race_register_block_entity", () -> new BlockEntityType<>(RaceRegisterBlockEntity::new, Set.of(BlockRegistry.RACE_REGISTER_BLOCK)));

    public static final RegistryObject<BlockEntityType<GateEntity>> GATE = BLOCK_ENTITIES
            .register("gate_entity", () -> new BlockEntityType<>(GateEntity::new, Set.of(BlockRegistry.GATE_DOOR)));
}

package net.tracen.umapyoi.item.data;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.registry.LazyRegistrar;
import net.tracen.umapyoi.registry.RegistryObject;
import net.tracen.umapyoi.registry.factors.FactorData;
import net.tracen.umapyoi.registry.umadata.Growth;
import net.tracen.umapyoi.registry.umadata.UmaDataBasicStatus;
import net.tracen.umapyoi.registry.umadata.UmaDataExtraStatus;
import net.tracen.umapyoi.registry.umadata.UmaDataSkills;
import net.tracen.umapyoi.registry.umadata.UmaDataTraining;

import java.util.List;

public class DataComponentsTypeRegistry {
    public static final LazyRegistrar<DataComponentType<?>> DATA_COMPONENTS = LazyRegistrar
            .create(Registries.DATA_COMPONENT_TYPE, Umapyoi.MODID);

    public static final RegistryObject<DataComponentType<Identifier>> DATA_LOCATION =
            DATA_COMPONENTS.register("data_location",
                    () -> DataComponentType.<Identifier>builder()
                            .persistent(Identifier.CODEC)
                            .networkSynchronized(Identifier.STREAM_CODEC)
                            .build()
            );

    public static final RegistryObject<DataComponentType<Identifier>> IDENTIFIER =
            DATA_COMPONENTS.register("identifier",
                    () -> DataComponentType.<Identifier>builder()
                            .persistent(Identifier.CODEC)
                            .networkSynchronized(Identifier.STREAM_CODEC)
                            .build()
            );

    public static final RegistryObject<DataComponentType<GachaRankingData>> GACHA_RANKING =
            DATA_COMPONENTS.register("ranking",
                    () -> DataComponentType.<GachaRankingData>builder()
                            .persistent(GachaRankingData.CODEC)
                            .networkSynchronized(GachaRankingData.STREAM)
                            .build()
            );

    public static final RegistryObject<DataComponentType<UmaDataBasicStatus>> UMADATA_BASIC_STATUS =
            DATA_COMPONENTS.register("umadata_basic_status",
                    () -> DataComponentType.<UmaDataBasicStatus>builder()
                            .persistent(UmaDataBasicStatus.CODEC)
                            .networkSynchronized(UmaDataBasicStatus.STREAM)
                            .build()
            );

    public static final RegistryObject<DataComponentType<UmaDataBasicStatus>> UMADATA_MAX_BASIC_STATUS =
            DATA_COMPONENTS.register("umadata_max_basic_status",
                    () -> DataComponentType.<UmaDataBasicStatus>builder()
                            .persistent(UmaDataBasicStatus.CODEC)
                            .networkSynchronized(UmaDataBasicStatus.STREAM)
                            .build()
            );

    public static final RegistryObject<DataComponentType<UmaDataBasicStatus>> UMADATA_STATUS_RATE =
            DATA_COMPONENTS.register("umadata_status_rate",
                    () -> DataComponentType.<UmaDataBasicStatus>builder()
                            .persistent(UmaDataBasicStatus.CODEC)
                            .networkSynchronized(UmaDataBasicStatus.STREAM)
                            .build()
            );

    public static final RegistryObject<DataComponentType<UmaDataExtraStatus>> UMADATA_EXTRA_STATUS =
            DATA_COMPONENTS.register("umadata_extra_status",
                    () -> DataComponentType.<UmaDataExtraStatus>builder()
                            .persistent(UmaDataExtraStatus.CODEC)
                            .networkSynchronized(UmaDataExtraStatus.STREAM)
                            .build()
            );

    public static final RegistryObject<DataComponentType<UmaDataSkills>> UMADATA_SKILLS =
            DATA_COMPONENTS.register("umadata_skills",
                    () -> DataComponentType.<UmaDataSkills>builder()
                            .persistent(UmaDataSkills.CODEC)
                            .networkSynchronized(UmaDataSkills.STREAM)
                            .build()
            );

    public static final RegistryObject<DataComponentType<UmaDataTraining>> UMADATA_TRAINING =
            DATA_COMPONENTS.register("umadata_training",
                    () -> DataComponentType.<UmaDataTraining>builder()
                            .persistent(UmaDataTraining.CODEC)
                            .networkSynchronized(UmaDataTraining.STREAM)
                            .build()
            );

    public static final RegistryObject<DataComponentType<Growth>> GROWTH =
            DATA_COMPONENTS.register("growth",
                    () -> DataComponentType.<Growth>builder()
                            .persistent(Growth.CODEC)
                            .networkSynchronized(Growth.STREAM)
                            .build()
            );

    public static final RegistryObject<DataComponentType<List<FactorData>>> FACTOR_DATA =
            DATA_COMPONENTS.register("factor_data",
                    () -> DataComponentType.<List<FactorData>>builder()
                            .persistent(FactorData.CODEC.listOf())
                            .networkSynchronized(FactorData.STREAM.apply(ByteBufCodecs.list()))
                            .build()
            );
}

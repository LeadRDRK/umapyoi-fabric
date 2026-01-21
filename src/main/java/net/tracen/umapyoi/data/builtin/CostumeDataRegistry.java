package net.tracen.umapyoi.data.builtin;

import net.minecraft.resources.ResourceLocation;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.registry.LazyRegistrar;
import net.tracen.umapyoi.registry.RegistryObject;
import net.tracen.umapyoi.registry.cosmetics.CosmeticData;

public class CostumeDataRegistry {
    public static final LazyRegistrar<CosmeticData> COSTUME_DATA = LazyRegistrar.create(
            CosmeticData.REGISTRY_KEY,
            Umapyoi.MODID);

    public static final RegistryObject<CosmeticData> COMMON_COSTUME = COSTUME_DATA.register("common_costume",
            () -> new CosmeticData(CosmeticData.COMMON_COSTUME));

    public static final RegistryObject<CosmeticData> STARTING_FUTURE = COSTUME_DATA.register("starting_future",
            () -> new CosmeticData(
                    ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "common_uma"),
                    ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "common_uma_flat"),
                    ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "common_uma"),
                    ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "common_uma_flat")
            ));

    public static final RegistryObject<CosmeticData> KINDERGARTEN_UNIFORM = COSTUME_DATA.register("kindergarten_uniform",
            () -> new CosmeticData(ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "kindergarten_uniform")));

    public static final RegistryObject<CosmeticData> KASAMATSU_TRAINING_UNIFORM = COSTUME_DATA.register("kasamatsu_training_uniform",
            () -> new CosmeticData(
                    ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "kasamatsu_training_uniform"),
                    ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "kasamatsu_training_uniform_flat"),
                    ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "kasamatsu_training_uniform"),
                    ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "kasamatsu_training_uniform")
            ));
}

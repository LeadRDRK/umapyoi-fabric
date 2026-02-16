package net.tracen.umapyoi.data.builtin;

import net.minecraft.resources.Identifier;
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
                    Identifier.fromNamespaceAndPath(Umapyoi.MODID, "common_uma"),
                    Identifier.fromNamespaceAndPath(Umapyoi.MODID, "common_uma_flat"),
                    Identifier.fromNamespaceAndPath(Umapyoi.MODID, "common_uma"),
                    Identifier.fromNamespaceAndPath(Umapyoi.MODID, "common_uma_flat")
            ));

    public static final RegistryObject<CosmeticData> KINDERGARTEN_UNIFORM = COSTUME_DATA.register("kindergarten_uniform",
            () -> new CosmeticData(Identifier.fromNamespaceAndPath(Umapyoi.MODID, "kindergarten_uniform")));

    public static final RegistryObject<CosmeticData> KASAMATSU_TRAINING_UNIFORM = COSTUME_DATA.register("kasamatsu_training_uniform",
            () -> new CosmeticData(
                    Identifier.fromNamespaceAndPath(Umapyoi.MODID, "kasamatsu_training_uniform"),
                    Identifier.fromNamespaceAndPath(Umapyoi.MODID, "kasamatsu_training_uniform_flat"),
                    Identifier.fromNamespaceAndPath(Umapyoi.MODID, "kasamatsu_training_uniform"),
                    Identifier.fromNamespaceAndPath(Umapyoi.MODID, "kasamatsu_training_uniform")
            ));
}

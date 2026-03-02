package net.tracen.umapyoi.container;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.registry.LazyRegistrar;
import net.tracen.umapyoi.registry.RegistryObject;

public class ContainerRegistry {
    public static final LazyRegistrar<MenuType<?>> CONTAINER_TYPES = LazyRegistrar
            .create(Registries.MENU, Umapyoi.MODID);

    public static final RegistryObject<MenuType<ThreeGoddessContainer>> THREE_GODDESS = CONTAINER_TYPES
            .register("three_goddess", () -> new ExtendedScreenHandlerType<>(ThreeGoddessContainer::new));

    public static final RegistryObject<MenuType<TrainingFacilityContainer>> TRAINING_FACILITY = CONTAINER_TYPES
            .register("training_facility", () -> new ExtendedScreenHandlerType<>(TrainingFacilityContainer::new));

    public static final RegistryObject<MenuType<SkillLearningMenu>> SKILL_LEARNING_TABLE = CONTAINER_TYPES
            .register("skill_learning_table", () -> new MenuType<>(SkillLearningMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final RegistryObject<MenuType<RetireRegisterMenu>> RETIRE_REGISTER = CONTAINER_TYPES
            .register("retire_register", () -> new MenuType<>(RetireRegisterMenu::new, FeatureFlags.DEFAULT_FLAGS));
    
    public static final RegistryObject<MenuType<DisassemblyBlockMenu>> DISASSEMBLY_BLOCK = CONTAINER_TYPES
            .register("disassembly_block", () -> new MenuType<>(DisassemblyBlockMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final RegistryObject<MenuType<UmaSelectMenu>> UMA_SELECT_MENU = CONTAINER_TYPES
            .register("uma_select_menu", () -> new MenuType<>(UmaSelectMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final RegistryObject<MenuType<FactorDecomposeMenu>> FACTOR_DECOMPOSE_MENU = CONTAINER_TYPES
            .register("factor_decompose_menu", () -> new MenuType<>(FactorDecomposeMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final RegistryObject<MenuType<FactorResearchMenu>> FACTOR_RESEARCH_MENU = CONTAINER_TYPES
            .register("factor_research_menu", () -> new MenuType<>(FactorResearchMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final RegistryObject<MenuType<RaceContainer>> RACE_REGISTER = CONTAINER_TYPES
            .register("race_container", () -> new ExtendedScreenHandlerType<>(RaceContainer::new));
}

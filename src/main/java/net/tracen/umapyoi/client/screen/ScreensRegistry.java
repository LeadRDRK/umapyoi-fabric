package net.tracen.umapyoi.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.tracen.umapyoi.container.ContainerRegistry;

@Environment(EnvType.CLIENT)
public class ScreensRegistry {
    public static void register() {
        MenuScreens.register(ContainerRegistry.THREE_GODDESS.get(), ThreeGoddessScreen::new);
        MenuScreens.register(ContainerRegistry.TRAINING_FACILITY.get(), TrainingFacilityScreen::new);
        MenuScreens.register(ContainerRegistry.SKILL_LEARNING_TABLE.get(), SkillLearningScreen::new);
        MenuScreens.register(ContainerRegistry.RETIRE_REGISTER.get(), RetireRegisterScreen::new);
        MenuScreens.register(ContainerRegistry.DISASSEMBLY_BLOCK.get(), DisassemblyBlockScreen::new);
        MenuScreens.register(ContainerRegistry.UMA_SELECT_MENU.get(), UmaSelectScreen::new);
        MenuScreens.register(ContainerRegistry.RACE_SELECT_MENU.get(), RaceSelectScreen::new);
        MenuScreens.register(ContainerRegistry.RACE_REGISTER.get(), RaceScreen::new);
        MenuScreens.register(ContainerRegistry.FACTOR_DECOMPOSE_MENU.get(), FactorDecomposeScreen::new);
        MenuScreens.register(ContainerRegistry.FACTOR_RESEARCH_MENU.get(), FactorResearchScreen::new);
    }
}

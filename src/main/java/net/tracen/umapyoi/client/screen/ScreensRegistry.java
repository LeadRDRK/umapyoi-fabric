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
    }
}

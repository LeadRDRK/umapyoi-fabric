package net.tracen.umapyoi.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.command.commands.GiveUmaSoul;
import net.tracen.umapyoi.command.commands.ModifyUmaSoul;
import net.tracen.umapyoi.command.utils.IntWithDefault;

public class CommandRegistry {
    public static void register() {
        ArgumentTypeRegistry.registerArgumentType(Umapyoi.id("int_with_default"),
                IntWithDefault.class, IntWithDefault.IntWithDefaultArgumentInfo.INSTANCE);

        CommandRegistrationCallback.EVENT.register((dispatcher, context, selection) -> {
            LiteralArgumentBuilder<CommandSourceStack> rootBuilder = Commands.literal("umapyoi");

            rootBuilder = GiveUmaSoul.registry(rootBuilder);
            ModifyUmaSoul.registry(rootBuilder);

            dispatcher.register(rootBuilder);
        });
    }
}

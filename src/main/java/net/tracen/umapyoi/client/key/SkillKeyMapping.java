package net.tracen.umapyoi.client.key;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.network.SelectSkillPacket;
import net.tracen.umapyoi.network.UseSkillPacket;

import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class SkillKeyMapping {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(Umapyoi.MODID, "umapyoi"));

    public static final KeyMapping KEY_USE_SKILL = new KeyMapping(
            "key.umapyoi.use_skill",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_BRACKET,
            CATEGORY
    );
    public static final KeyMapping KEY_FORMER_SKILL = new KeyMapping(
            "key.umapyoi.select_former_skill",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UP,
            CATEGORY
    );
    public static final KeyMapping KEY_LATTER_SKILL = new KeyMapping(
            "key.umapyoi.select_latter_skill",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_DOWN,
            CATEGORY
    );

    public static void onEndClientTick(Minecraft client) {
        while (KEY_USE_SKILL.consumeClick()) {
            ClientPlayNetworking.send(new UseSkillPacket());
        }
        while (KEY_FORMER_SKILL.consumeClick()) {
            ClientPlayNetworking.send(new SelectSkillPacket(SelectSkillPacket.FORMER_SLOT));
        }
        while (KEY_LATTER_SKILL.consumeClick()) {
            ClientPlayNetworking.send(new SelectSkillPacket(SelectSkillPacket.LATTER_SLOT));
        }
    }
}
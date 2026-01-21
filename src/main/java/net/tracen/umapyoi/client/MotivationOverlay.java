package net.tracen.umapyoi.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.utils.UmaSoulUtils;

@Environment(EnvType.CLIENT)
public class MotivationOverlay implements HudRenderCallback {
    public static final MotivationOverlay INSTANCE = new MotivationOverlay();
    private final Minecraft minecraft = Minecraft.getInstance();

    public MotivationOverlay() {
    }

    private static final ResourceLocation HUD = ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/gui/motivations.png");

    @Override
    public void onHudRender(GuiGraphics guiGraphics, DeltaTracker tickCounter) {
        if (!Umapyoi.CONFIG.OVERLAY_SWITCH())
            return;

        if (minecraft.options.hideGui)
            return;

        var window = minecraft.getWindow();
        int x = window.getGuiScaledWidth() / 2;
        int y = window.getGuiScaledHeight();

        Player player = minecraft.player;
        if (player.isSpectator())
            return;

        if (!UmapyoiAPI.getUmaSoul(player).isEmpty()) {
            switch (UmaSoulUtils.getMotivation(UmapyoiAPI.getUmaSoul(player))) {
            case BAD -> {
                guiGraphics.blit(HUD, x + 118, y - 37, 0, 60, 64, 14, 64, 96);
                guiGraphics.drawString(this.minecraft.font, Component.translatable("umapyoi.motivation.bad"), x + 132,
                        y - 34, 0XFFFFFF, false);
            }
            case DOWN -> {
                guiGraphics.blit(HUD, x + 118, y - 37, 0, 45, 64, 14, 64, 96);
                guiGraphics.drawString(this.minecraft.font, Component.translatable("umapyoi.motivation.down"), x + 132,
                        y - 34, 0XFFFFFF, false);
            }
            case NORMAL -> {
                guiGraphics.blit(HUD, x + 118, y - 37, 0, 30, 64, 14, 64, 96);
                guiGraphics.drawString(this.minecraft.font, Component.translatable("umapyoi.motivation.normal"), x + 132,
                        y - 34, 0XFFFFFF, false);
            }
            case GOOD -> {
                guiGraphics.blit(HUD, x + 118, y - 37, 0, 15, 64, 14, 64, 96);
                guiGraphics.drawString(this.minecraft.font, Component.translatable("umapyoi.motivation.good"), x + 132,
                        y - 34, 0XFFFFFF, false);
            }

            case PERFECT -> {
                guiGraphics.blit(HUD, x + 118, y - 37, 0, 0, 64, 14, 64, 96);
                guiGraphics.drawString(this.minecraft.font, Component.translatable("umapyoi.motivation.perfect"), x + 132,
                        y - 34, 0XFFFFFF, false);
            }
            default -> throw new IllegalArgumentException(
                    "Unexpected value: " + UmaSoulUtils.getMotivation(UmapyoiAPI.getUmaSoul(player)));
            }

        }
    }

}

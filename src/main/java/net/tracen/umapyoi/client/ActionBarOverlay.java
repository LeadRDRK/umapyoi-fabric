package net.tracen.umapyoi.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.utils.UmaSoulUtils;

@Environment(EnvType.CLIENT)
public class ActionBarOverlay implements HudElement {
    public static final Identifier ID = Umapyoi.id("action_bar_overlay");
    private final Minecraft minecraft = Minecraft.getInstance();

    public ActionBarOverlay() {
    }

    private static final Identifier HUD = Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/gui/actionbar.png");

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        if (!Umapyoi.CONFIG.OVERLAY_SWITCH)
            return;

        if (minecraft.gui.hud.isHidden())
            return;

        var window = minecraft.getWindow();
        int x = window.getGuiScaledWidth();
        int y = window.getGuiScaledHeight();

        Player player = minecraft.player;
        if (player.isSpectator())
            return;

        if (!UmapyoiAPI.getUmaSoul(player).isEmpty()) {
            renderSkill(UmapyoiAPI.getUmaSoul(player), guiGraphics, x - 8, y - 64);
        }
    }

    private void renderSkill(ItemStack soul, GuiGraphicsExtractor guiGraphics, int x, int y) {
        int ap = UmaSoulUtils.getActionPoint(soul);
        int maxAp = UmaSoulUtils.getMaxActionPoint(soul);
        if (ap == maxAp)
            return;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HUD, x, y - 128, 11, 0, 5, 128, 16, 128);
        int apbar = ap != 0 ? ap * 128 / maxAp : 0;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HUD, x, y - apbar, 6, 128 - apbar, 5, apbar, 16, 128);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HUD, x - 7, y - 3 - apbar, 0, 0, 6, 7, 16, 128);
        String str = String.valueOf(ap);
        guiGraphics.text(this.minecraft.font, str,
                x - 8 - this.minecraft.font.width(str), y - 3 - apbar,
                0xFFFFFFFF, false);
    }
}

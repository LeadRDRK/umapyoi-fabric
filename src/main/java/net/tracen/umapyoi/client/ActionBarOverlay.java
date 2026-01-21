package net.tracen.umapyoi.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.utils.UmaSoulUtils;

@Environment(EnvType.CLIENT)
public class ActionBarOverlay implements HudRenderCallback {
    public static final ActionBarOverlay INSTANCE = new ActionBarOverlay();
    private final Minecraft minecraft = Minecraft.getInstance();

    public ActionBarOverlay() {
    }

    private static final ResourceLocation HUD = ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/gui/actionbar.png");

    @Override
    public void onHudRender(GuiGraphics drawContext, DeltaTracker tickCounter) {
        if (!Umapyoi.CONFIG.OVERLAY_SWITCH())
            return;

        if (minecraft.options.hideGui)
            return;

        var window = minecraft.getWindow();
        int x = window.getGuiScaledWidth();
        int y = window.getGuiScaledHeight();

        Player player = minecraft.player;
        if (player.isSpectator())
            return;

        if (!UmapyoiAPI.getUmaSoul(player).isEmpty()) {
            renderSkill(UmapyoiAPI.getUmaSoul(player), drawContext, x - 8, y - 64);
        }
    }

    private void renderSkill(ItemStack soul, GuiGraphics guiGraphics, int x, int y) {
        int ap = UmaSoulUtils.getActionPoint(soul);
        int maxAp = UmaSoulUtils.getMaxActionPoint(soul);
        if (ap == maxAp)
            return;
        guiGraphics.blit(HUD, x, y - 128, 11, 0, 5, 128, 16, 128);
        int apbar = ap != 0 ? ap * 128 / maxAp : 0;
        guiGraphics.blit(HUD, x, y - apbar, 6, 128 - apbar, 5, apbar, 16, 128);
        guiGraphics.blit(HUD, x - 7, y - 3 - apbar, 0, 0, 6, 7, 16, 128);
        String str = String.valueOf(ap);
        guiGraphics.drawString(this.minecraft.font, str,
                x - 8 - this.minecraft.font.width(str), y - 3 - apbar,
                0xFFFFFF, false);
    }
}

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
import net.tracen.umapyoi.registry.UmaSkillRegistry;
import net.tracen.umapyoi.registry.skills.UmaSkill;
import net.tracen.umapyoi.utils.UmaSoulUtils;

@Environment(EnvType.CLIENT)
public class SkillOverlay implements HudRenderCallback {
    public static final SkillOverlay INSTANCE = new SkillOverlay();
    private final Minecraft minecraft = Minecraft.getInstance();

    public SkillOverlay() {
    }

    private static final ResourceLocation HUD = ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/gui/skill_hud.png");

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
            guiGraphics.blit(HUD, x + 102, y - 21, 0, 0, 96, 20, 128, 64);
            renderSkill(UmapyoiAPI.getUmaSoul(player), guiGraphics, x + 102, y - 21);
        }
    }

    private void renderSkill(ItemStack soul, GuiGraphics guiGraphics, int x, int y) {
        UmaSkill skill = UmaSkillRegistry.REGISTRY.get().get(UmaSoulUtils.getSelectedSkill(soul));
        if (skill != null) {
            switch (skill.getType()) {
            case BUFF -> guiGraphics.blit(HUD, x + 3, y + 2, 0, 48, 16, 16, 128, 64);
            case HINDER -> guiGraphics.blit(HUD, x + 3, y + 2, 16, 48, 16, 16, 128, 64);
            case HEAL -> guiGraphics.blit(HUD, x + 3, y + 2, 32, 48, 16, 16, 128, 64);
            case PASSIVE -> guiGraphics.blit(HUD, x + 3, y + 2, 48, 48, 16, 16, 128, 64);
            default -> throw new IllegalArgumentException("Unexpected value: " + skill.getType());
            }
            guiGraphics.drawString(this.minecraft.font, skill.getDescription(), x + 22, y + 6, 0x794016, false);
        } else {
            guiGraphics.blit(HUD, x, y, 0, 20, 96, 20, 128, 64);
        }
    }

}

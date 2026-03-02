package net.tracen.umapyoi.client.screen.setting;

import static net.tracen.umapyoi.client.SkillOverlay.renderSkill;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.client.MotivationOverlay;
import net.tracen.umapyoi.client.SkillOverlay;
import net.tracen.umapyoi.registry.UmaSkillRegistry;

import org.lwjgl.glfw.GLFW;

public class OverlayScreen extends Screen {
    private Button buttonSave;
    private Button buttonDiscard;
    private double skillX;
    private double skillY;
    private double motivationX;
    private double motivationY;
    private int x;
    private DragOnType isDraggingOn = null;
    private DragOnType lastClicked = null;

    private enum DragOnType {
        SKILL, MOTIVATION;
    }

    public OverlayScreen() {
        super(Component.literal("Setting"));
        this.skillX = Umapyoi.CONFIG.TOPLEFT_COORD_SKILL_X();
        this.skillY = Umapyoi.CONFIG.TOPLEFT_COORD_SKILL_Y();
        this.motivationX = Umapyoi.CONFIG.TOPLEFT_COORD_MOTIVATION_X();
        this.motivationY = Umapyoi.CONFIG.TOPLEFT_COORD_MOTIVATION_Y();
        this.lastClicked = null;
    }

    private void close() {
        this.onClose();
        Minecraft.getInstance().setScreen(null);
    }

    @Override
    protected void init() {
        super.init();
        this.x = this.width / 2;
        this.buttonSave = Button.builder(Component.translatable("setting.umapyoi.save"), b -> {
            Umapyoi.CONFIG.TOPLEFT_COORD_SKILL_X((int) (this.skillX + x) - x);
            Umapyoi.CONFIG.TOPLEFT_COORD_SKILL_Y((int) (this.skillY + this.height) - this.height);
            Umapyoi.CONFIG.TOPLEFT_COORD_MOTIVATION_X((int) (this.motivationX + x) - x);
            Umapyoi.CONFIG.TOPLEFT_COORD_MOTIVATION_Y((int) (this.motivationY + this.height) - this.height);
            close();
        }).bounds(this.width / 2 - 75, 10, 70, 20).build();
        this.buttonDiscard = Button.builder(Component.translatable("setting.umapyoi.discard").withStyle(ChatFormatting.RED), b -> close())
                .bounds(this.width / 2 + 5, 10, 70, 20).build();
        this.addRenderableWidget(buttonSave);
        this.addRenderableWidget(buttonDiscard);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (buttonSave.isMouseOver(pMouseX, pMouseY) || buttonDiscard.isMouseOver(pMouseX, pMouseY)) {
            return super.mouseClicked(pMouseX, pMouseY, pButton);
        }
        if (pMouseX >= x + this.skillX && pMouseX <= x + this.skillX + 96 && pMouseY >= this.height + this.skillY && pMouseY <= this.height + this.skillY + 20) {
            this.isDraggingOn = DragOnType.SKILL;
            this.lastClicked = DragOnType.SKILL;
            return true;
        }
        if (pMouseX >= x + this.motivationX && pMouseX <= x + this.motivationX + 64 && pMouseY >= this.height + this.motivationY && pMouseY <= this.height + this.motivationY + 14) {
            this.isDraggingOn = DragOnType.MOTIVATION;
            this.lastClicked = DragOnType.MOTIVATION;
            return true;
        }
        this.isDraggingOn = null;
        this.lastClicked = null;
        return true;
        // this.skillX = (int) (pMouseX - this.width / 2d);
        // this.skillY = (int) (pMouseY - this.height);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == GLFW.GLFW_KEY_R) {
            var config = Umapyoi.CONFIG;
            this.skillX = (int) config.optionForKey(config.keys.TOPLEFT_COORD_SKILL_X).defaultValue();
            this.skillY = (int) config.optionForKey(config.keys.TOPLEFT_COORD_SKILL_Y).defaultValue();
            this.motivationX = (int) config.optionForKey(config.keys.TOPLEFT_COORD_MOTIVATION_X).defaultValue();
            this.motivationY = (int) config.optionForKey(config.keys.TOPLEFT_COORD_MOTIVATION_Y).defaultValue();
            return true;
        }
        if (lastClicked == null) return super.keyPressed(pKeyCode, pScanCode, pModifiers);
        switch (this.lastClicked) {
            case SKILL:
                if (pKeyCode == GLFW.GLFW_KEY_UP) skillY += 1;
                if (pKeyCode == GLFW.GLFW_KEY_DOWN) skillY -= 1;
                if (pKeyCode == GLFW.GLFW_KEY_LEFT) skillX -= 1;
                if (pKeyCode == GLFW.GLFW_KEY_RIGHT) skillX += 1;
            case MOTIVATION:
                if (pKeyCode == GLFW.GLFW_KEY_UP) motivationY += 1;
                if (pKeyCode == GLFW.GLFW_KEY_DOWN) motivationY -= 1;
                if (pKeyCode == GLFW.GLFW_KEY_LEFT) motivationX -= 1;
                if (pKeyCode == GLFW.GLFW_KEY_RIGHT) motivationX += 1;
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.isDraggingOn != null) {
            switch (this.isDraggingOn) {
                case SKILL:
                    this.skillX += pDragX;
                    this.skillY += pDragY;
                    break;
                case MOTIVATION:
                    this.motivationX += pDragX;
                    this.motivationY += pDragY;
            }
            return true;
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        this.isDraggingOn = null;
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean isPauseScreen() {
        return !Minecraft.getInstance().hasSingleplayerServer();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.blit(SkillOverlay.HUD, (int) (x + this.skillX), (int) (this.height + this.skillY), 0, 0, 96, 20, 128, 64);
        renderSkill(UmaSkillRegistry.BASIC_PACE.get(), Minecraft.getInstance().font, pGuiGraphics, (int) (x + this.skillX), (int) (this.height + this.skillY));
        pGuiGraphics.blit(MotivationOverlay.HUD, (int) (x + this.motivationX), (int) (this.height + this.motivationY), 0, 0, 64, 14, 64, 96);
        pGuiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("umapyoi.motivation.perfect"),
                (int) (x + this.motivationX + 14), (int) (this.height + this.motivationY + 3), 0XFFFFFF);

    }
}

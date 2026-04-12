package net.tracen.umapyoi.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.container.DisassemblyBlockMenu;

public class DisassemblyBlockScreen extends AbstractContainerScreen<DisassemblyBlockMenu> {

    private static final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath(Umapyoi.MODID,
            "textures/gui/disassembly_gui.png");

    public DisassemblyBlockScreen(DisassemblyBlockMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn, 176, 176);
        this.leftPos = 0;
        this.topPos = 0;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, final int mouseX, final int mouseY, float partialTicks) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
        this.extractTooltip(guiGraphics, mouseX, mouseY);

    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        guiGraphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY - 3, 0xFFFFFFFF, false);
        guiGraphics.text(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 0xFF404040, false);
    }

    @Override
    public void extractBackground(final GuiGraphicsExtractor guiGraphics, final int mouseX, final int mouseY, final float a) {
        // Render UI background
        if (this.minecraft == null) {
            return;
        }

        this.extractTransparentBackground(guiGraphics);

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE,
                this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight,
                BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);

        if (this.menu.getSlot(0).hasItem() && !this.menu.getSlot(1).hasItem())
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE,
                    this.leftPos + 74, this.topPos + 57, 176, 0, 29, 19,
                    BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
    }

}

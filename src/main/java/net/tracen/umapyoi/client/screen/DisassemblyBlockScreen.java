package net.tracen.umapyoi.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.container.DisassemblyBlockMenu;

public class DisassemblyBlockScreen extends AbstractContainerScreen<DisassemblyBlockMenu> {

    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID,
            "textures/gui/disassembly_gui.png");

    public DisassemblyBlockScreen(DisassemblyBlockMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 176;
    }

    @Override
    public void render(GuiGraphics guiGraphics, final int mouseX, final int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY - 3, 0xFFFFFFFF, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 0xFF404040, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Render UI background
        if (this.minecraft == null) {
            return;
        }

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE,
                this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight,
                BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);

        if (this.menu.getSlot(0).hasItem() && !this.menu.getSlot(1).hasItem())
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE,
                    this.leftPos + 74, this.topPos + 57, 176, 0, 29, 19,
                    BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
    }

}

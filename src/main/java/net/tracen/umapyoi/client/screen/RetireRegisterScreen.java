package net.tracen.umapyoi.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.container.RetireRegisterMenu;
import net.tracen.umapyoi.item.UmaSoulItem;
import net.tracen.umapyoi.utils.UmaSoulUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils;

public class RetireRegisterScreen extends AbstractContainerScreen<RetireRegisterMenu> {

    private static final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath(Umapyoi.MODID,
            "textures/gui/retire_gui.png");

    public RetireRegisterScreen(RetireRegisterMenu screenContainer, Inventory inv, Component titleIn) {
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
        guiGraphics.pose().pushMatrix();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE,
                this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight,
                BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);

        ItemStack input = this.getMenu().getSlot(0).getItem();
        if (this.menu.getSlot(0).hasItem() && !this.menu.getSlot(1).hasItem())
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE,
                    this.leftPos + 74, this.topPos + 57, 176, 0, 29, 19,
                    BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);

        else if (input.getItem() instanceof UmaSoulItem) {
            var status = UmaSoulUtils.getProperty(input);
            guiGraphics.text(this.font, UmaStatusUtils.getStatusLevel(status.speed()), this.leftPos + 21,
                    this.topPos + 31, 0xFF40C100, false);
            guiGraphics.text(this.font, UmaStatusUtils.getStatusLevel(status.stamina()), this.leftPos + 52,
                    this.topPos + 31, 0xFF40C100, false);
            guiGraphics.text(this.font, UmaStatusUtils.getStatusLevel(status.strength()), this.leftPos + 83,
                    this.topPos + 31, 0xFF40C100, false);
            guiGraphics.text(this.font, UmaStatusUtils.getStatusLevel(status.guts()), this.leftPos + 114,
                    this.topPos + 31, 0xFF40C100, false);
            guiGraphics.text(this.font, UmaStatusUtils.getStatusLevel(status.wisdom()), this.leftPos + 146,
                    this.topPos + 31, 0xFF40C100, false);
        }
        guiGraphics.pose().popMatrix();
    }

}

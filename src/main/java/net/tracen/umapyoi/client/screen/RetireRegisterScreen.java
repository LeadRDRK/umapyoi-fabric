package net.tracen.umapyoi.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.container.RetireRegisterMenu;
import net.tracen.umapyoi.item.UmaSoulItem;
import net.tracen.umapyoi.utils.UmaSoulUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils.StatusType;

public class RetireRegisterScreen extends AbstractContainerScreen<RetireRegisterMenu> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Umapyoi.MODID,
            "textures/gui/retire_gui.png");

    public RetireRegisterScreen(RetireRegisterMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 176;
    }

    @Override
    public void render(GuiGraphics guiGraphics, final int mouseX, final int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY - 3, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Render UI background
        if (this.minecraft == null) {
            return;
        }
        guiGraphics.pose().pushPose();
        guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        ItemStack input = this.getMenu().getSlot(0).getItem();
        if (this.menu.getSlot(0).hasItem() && !this.menu.getSlot(1).hasItem())
            guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + 74, this.topPos + 57, 176, 0, 29, 19);

        else if (input.getItem() instanceof UmaSoulItem) {
            int[] status = UmaSoulUtils.getProperty(input);
            guiGraphics.drawString(this.font, UmaStatusUtils.getStatusLevel(status[StatusType.SPEED.getId()]), this.leftPos + 21,
                    this.topPos + 31, 0x40C100, false);
            guiGraphics.drawString(this.font, UmaStatusUtils.getStatusLevel(status[StatusType.STAMINA.getId()]), this.leftPos + 52,
                    this.topPos + 31, 0x40C100, false);
            guiGraphics.drawString(this.font, UmaStatusUtils.getStatusLevel(status[StatusType.STRENGTH.getId()]), this.leftPos + 83,
                    this.topPos + 31, 0x40C100, false);
            guiGraphics.drawString(this.font, UmaStatusUtils.getStatusLevel(status[StatusType.GUTS.getId()]), this.leftPos + 114,
                    this.topPos + 31, 0x40C100, false);
            guiGraphics.drawString(this.font, UmaStatusUtils.getStatusLevel(status[StatusType.WISDOM.getId()]), this.leftPos + 146,
                    this.topPos + 31, 0x40C100, false);
        }
        guiGraphics.pose().popPose();
    }

}

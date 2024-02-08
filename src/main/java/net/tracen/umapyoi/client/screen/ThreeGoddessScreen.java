package net.tracen.umapyoi.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.client.model.SimpleBedrockModel;
import net.tracen.umapyoi.container.ThreeGoddessContainer;
import net.tracen.umapyoi.registry.UmaDataRegistry;
import net.tracen.umapyoi.utils.ClientUtils;

import org.joml.Quaternionf;

public class ThreeGoddessScreen extends AbstractContainerScreen<ThreeGoddessContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Umapyoi.MODID,
            "textures/gui/three_goddess.png");

    public ThreeGoddessScreen(ThreeGoddessContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 220;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        this.renderModels(guiGraphics);
    }

    private static Quaternionf fatherQuaternion = new Quaternionf().rotateXYZ(
            (float)Math.toRadians(30f),
            (float)Math.toRadians(-45f),
            0
    );
    private static Quaternionf motherQuaternion = new Quaternionf().rotateXYZ(
            (float)Math.toRadians(30f),
            (float)Math.toRadians(45f),
            0
    );
    protected void renderModels(GuiGraphics guiGraphics) {
        ItemStack fatherFactor = this.menu.tileEntity.getItem(1);
        ItemStack motherFactor = this.menu.tileEntity.getItem(2);
        if (!fatherFactor.isEmpty()) {
            ResourceLocation name = new ResourceLocation(fatherFactor.getTag().getString("name"));
            renderModel(guiGraphics, this.leftPos + 33, this.topPos + 55, 25, fatherQuaternion, name);
        }
        if (!motherFactor.isEmpty()) {
            ResourceLocation name = new ResourceLocation(motherFactor.getTag().getString("name"));
            renderModel(guiGraphics, this.leftPos + 142, this.topPos + 55, 25, motherQuaternion, name);
        }
    }

    protected void renderModel(GuiGraphics guiGraphics, int pPosX, int pPosY, int pScale, Quaternionf pQuaternion, ResourceLocation name) {
        if (!ClientUtils.getClientUmaDataRegistry().containsKey(name)) {
            name = UmaDataRegistry.COMMON_UMA.getId();
        }
        SimpleBedrockModel model = new SimpleBedrockModel(ClientUtils.getModelPOJO(name));
        ClientUtils.renderModelInInventory(guiGraphics, pPosX, pPosY, pScale, pQuaternion, model, name);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title,
                Math.round((this.imageWidth / 2.0F) - (this.font.width(this.title.getVisualOrderText()) / 2.0F)),
                this.titleLabelY - 3, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        // Render UI background
        if (this.minecraft == null) {
            return;
        }
        guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        // Render progress bar
        int l = this.menu.getProgressionScaled();
        guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + 9, this.topPos + 112, 0, 220, l + 1, 5);

    }
}

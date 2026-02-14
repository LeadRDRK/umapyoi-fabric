package net.tracen.umapyoi.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.client.model.SimpleBedrockModel;
import net.tracen.umapyoi.client.screen.pip.GuiBedrockModelRenderer;
import net.tracen.umapyoi.container.ThreeGoddessContainer;
import net.tracen.umapyoi.data.builtin.UmaDataRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.ClientUtils;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ThreeGoddessScreen extends AbstractContainerScreen<ThreeGoddessContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID,
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
            ResourceLocation name = fatherFactor
                    .getOrDefault(DataComponentsTypeRegistry.DATA_LOCATION.get(), UmaData.DEFAULT_UMA_ID);
            renderModel(guiGraphics, this.leftPos + 8, this.topPos + 34, 25f,
                    new Vector3f(0.2f, -1.64f, 0.0f), fatherQuaternion, name);
        }
        if (!motherFactor.isEmpty()) {
            ResourceLocation name = motherFactor
                    .getOrDefault(DataComponentsTypeRegistry.DATA_LOCATION.get(), UmaData.DEFAULT_UMA_ID);
            renderModel(guiGraphics, this.leftPos + 127, this.topPos + 34, 25f,
                    new Vector3f(-0.2f, -1.64f, 0.0f), motherQuaternion, name);
        }
    }

    protected void renderModel(GuiGraphics guiGraphics, int pPosX, int pPosY, float pScale,
                               Vector3f pTranslation, Quaternionf pQuaternion, ResourceLocation name) {
        if (!ClientUtils.getClientUmaDataRegistry().containsKey(name)) {
            name = UmaDataRegistry.COMMON_UMA.location();
        }
        SimpleBedrockModel model = new SimpleBedrockModel(ClientUtils.getModelPOJO(name));
        guiGraphics.guiRenderState.submitPicturesInPictureState(new GuiBedrockModelRenderer.RenderState(
                model, name, pTranslation, pQuaternion, pPosX, pPosY,
                pPosX + 40, pPosY + 62, pScale, guiGraphics.scissorStack.peek()
        ));
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title,
                Math.round((this.imageWidth / 2.0F) - (this.font.width(this.title.getVisualOrderText()) / 2.0F)),
                this.titleLabelY - 3, 0xFFFFFFFF, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 0xFF404040, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        // Render UI background
        if (this.minecraft == null) {
            return;
        }
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
        // Render progress bar
        int l = this.menu.getProgressionScaled();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + 9, this.topPos + 112, 0, 220, l + 1, 5, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);

    }
}

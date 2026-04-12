package net.tracen.umapyoi.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.client.model.SimpleBedrockModel;
import net.tracen.umapyoi.client.model.bedrock.BedrockPart;
import net.tracen.umapyoi.client.screen.pip.GuiBedrockModelRenderer;
import net.tracen.umapyoi.container.FactorDecomposeMenu;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.ClientUtils;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class FactorDecomposeScreen extends AbstractContainerScreen<FactorDecomposeMenu> {
    private static final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath(Umapyoi.MODID,
            "textures/gui/factor_decompose.png");

    public FactorDecomposeScreen(FactorDecomposeMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn, 176, 226);
        this.leftPos = 0;
        this.topPos = 0;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphic, final int mouseX, final int mouseY, float partialTicks) {
        super.extractRenderState(graphic, mouseX, mouseY, partialTicks);
        this.extractTooltip(graphic, mouseX, mouseY);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphic, int mouseX, int mouseY) {
        graphic.text(this.font, this.title, (this.imageWidth / 2) - (this.font.width(this.title.getVisualOrderText()) / 2), this.titleLabelY - 3, 0xFFFFFFFF);
        graphic.text(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 0xFF404040, false);
    }

    @Override
    public void extractBackground(final GuiGraphicsExtractor graphic, final int mouseX, final int mouseY, final float a) {
        // Render UI background
        if (this.minecraft == null) {
            return;
        }
        graphic.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + 20, this.topPos + 17, 176, 55, 64, 64, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
        this.renderUma(graphic);
        graphic.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
//        graphic.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + 15, this.topPos + 23, 88, 93, 25, 28, 128, 128);

        if (this.menu.getSlot(0).hasItem() && (!this.menu.getSlot(1).hasItem() || this.menu.isTaking()))
            graphic.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + 69, this.topPos + 94, 176, 0, 22, 15, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
    }

    protected void renderUma(GuiGraphicsExtractor graphic) {
        ItemStack rawStack = this.menu.getSlot(0).getItem();
        if (rawStack.isEmpty()) return;
        Identifier name = Optional.ofNullable(rawStack.get(DataComponentsTypeRegistry.DATA_LOCATION.get()))
                .orElse(UmaData.DEFAULT_UMA_ID);
        if (!ClientUtils.getClientUmaDataRegistry().containsKey(name)) name = UmaData.DEFAULT_UMA_ID;
        SimpleBedrockModel model = new SimpleBedrockModel(ClientUtils.getModelPOJO(name)) {
            @Override
            public List<BedrockPart> getShouldRender() {
                return new ArrayList<>(Stream.of("head", "hat").map(this.getModelMap()::get).filter(Objects::nonNull).toList());
            }
        };
        Optional.ofNullable(model.getModelMap().get("long_hair")).ifPresent(i -> i.xRot = (float) (Math.PI / 6f));
        int x = this.leftPos + 22;
        int y = this.topPos + 19;
        Vector3f translation = new Vector3f(0.0f, -0.38f, 0.0f);
        Quaternionf rotation = new Quaternionf().rotateXYZ((float) (Math.PI / 6f), (float) (-Math.PI / 4f), 0);
        graphic.guiRenderState.addPicturesInPictureState(new GuiBedrockModelRenderer.RenderState(
                model, name, translation, rotation, x, y,
                x + 60, y + 60, 50f, graphic.scissorStack.peek()
        ));
    }
}

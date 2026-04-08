package net.tracen.umapyoi.client.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.container.FactorResearchMenu;
import net.tracen.umapyoi.item.factor.FactorReport;
import net.tracen.umapyoi.registry.factors.UmaFactor;
import net.tracen.umapyoi.registry.factors.UmaFactorStack;

import java.util.ArrayList;
import java.util.List;

public class FactorResearchScreen extends ItemCombinerScreen<FactorResearchMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID,
            "textures/gui/factor_research.png");

    private static final int MAX_LEN = 96;
    private boolean needTooltip = false;

    public FactorResearchScreen(FactorResearchMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, BACKGROUND_TEXTURE);
    }

    @Override
    protected void renderLabels(GuiGraphics graphic, int mouseX, int mouseY) {
        graphic.drawString(this.font, this.title,
                (this.imageWidth / 2) - (this.font.width(this.title.getVisualOrderText()) / 2),
                this.titleLabelY - 3, 0xFFFFFF);
        graphic.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderErrorIcon(GuiGraphics graphic, int x, int y) {
        if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(this.menu.getResultSlot()).hasItem()) {
            graphic.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, x + 99, y + 45, this.imageWidth, 0, 28, 21, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
        }
    }

    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderContents(guiGraphics, mouseX, mouseY, partialTick);
        if (!this.menu.getSlot(1).hasItem()) return;

        List<UmaFactorStack> listFactors = FactorReport.getFactorStacks(this.menu.getSlot(1).getItem());
        if (listFactors.isEmpty()) return;
        UmaFactorStack stackFirst = listFactors.get(0);
        int v = 166 + 25 * switch (stackFirst.getFactor().getFactorType()) {
            case STATUS -> 0;
            case EXTRASTATUS -> 1;
            default -> 2;
        };
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + 28, this.topPos + 16, 0, v, 121, 25, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + 32, this.topPos + 20, 176, 21, 16, 16, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
        if (listFactors.size() == 1) {
            UmaFactor factor = stackFirst.getFactor();
            Component componentDesc = factor.getDescription(stackFirst).copy().withStyle(ChatFormatting.RESET);
            Component componentDetailDesc = factor.getDescriptionDetail(stackFirst).copy().withStyle(ChatFormatting.RESET);
            FormattedCharSequence sequenceDesc;
            FormattedCharSequence sequenceDetailDesc;
            needTooltip = false;
            if (this.font.width(componentDesc) > MAX_LEN) {
                needTooltip = true;
                sequenceDesc = truncate(componentDesc);
            } else {
                sequenceDesc = componentDesc.getVisualOrderText();
            }
            if (this.font.width(componentDetailDesc) > MAX_LEN) {
                needTooltip = true;
                sequenceDetailDesc = truncate(componentDetailDesc);
            } else {
                sequenceDetailDesc = componentDetailDesc.getVisualOrderText();
            }
            guiGraphics.drawString(this.font, sequenceDesc, leftPos + 50, topPos + 19, 4210752, false);
            guiGraphics.drawString(this.font, sequenceDetailDesc, leftPos + 50, topPos + 30, 4210752, false);
        } else {
            needTooltip = true;
            guiGraphics.drawString(this.font, Component.translatable("gui.umapyoi.multiple_factor", String.valueOf(listFactors.size())), leftPos + 50, topPos + 19, 4210752, false);
            guiGraphics.drawString(this.font, Component.translatable("gui.umapyoi.hover_for_tooltip"), leftPos + 50, topPos + 30, 4210752, false);
        }
    }

    public FormattedCharSequence truncate(Component sequence) {
        int ellipseWidth = this.font.width("...");
        int maxWidth = MAX_LEN - ellipseWidth;
        List<FormattedCharSequence> split = this.font.split(sequence, maxWidth);
        return FormattedCharSequence.composite(
                split.get(0), Component.literal("...").getVisualOrderText()
        );
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        if (needTooltip && this.isHovering(28, 16, 121, 25, pX, pY)) {
            if (!this.menu.getSlot(1).hasItem()) return;

            List<UmaFactorStack> listFactors = FactorReport.getFactorStacks(this.menu.getSlot(1).getItem());
            ArrayList<Component> components = new ArrayList<>();
            listFactors.stream().sorted(UmaFactorStack.UmaFactorStackComparator.INSTANCE).forEachOrdered(factor -> {
                switch (factor.getFactor().getFactorType()) {
                    case STATUS -> components.add(factor.getDescription().copy().withStyle(ChatFormatting.BLUE));
                    case UNIQUE -> components.add(factor.getDescription().copy().withStyle(ChatFormatting.GREEN));
                    case EXTRASTATUS -> components.add(factor.getDescription().copy().withStyle(ChatFormatting.RED));
                    default -> components.add(factor.getDescription().copy().withStyle(ChatFormatting.GRAY));
                }
                components.add(factor.getDescriptionDetail().copy().withStyle(ChatFormatting.DARK_GRAY));
            });
            pGuiGraphics.setComponentTooltipForNextFrame(this.font, components, pX, pY);
        }
    }
}

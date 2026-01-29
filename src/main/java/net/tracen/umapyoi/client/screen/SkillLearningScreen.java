package net.tracen.umapyoi.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.container.SkillLearningMenu;
import net.tracen.umapyoi.item.SkillBookItem;
import net.tracen.umapyoi.registry.skills.UmaSkill;
import net.tracen.umapyoi.registry.umadata.Growth;
import net.tracen.umapyoi.utils.UmaSoulUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils;

public class SkillLearningScreen extends ItemCombinerScreen<SkillLearningMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID,
            "textures/gui/skill_learning.png");

    public SkillLearningScreen(SkillLearningMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, BACKGROUND_TEXTURE);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title,
                (this.imageWidth / 2) - (this.font.width(this.title.getVisualOrderText()) / 2),
                this.titleLabelY - 3, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
        UmaSkill skill = this.getBookSkill();
        if (skill != null) {
            guiGraphics.drawString(this.font, skill.getDescription(), 51, 20, 0x794016, false);
            ItemStack soul = this.getMenu().getSlot(0).hasItem() ? this.getMenu().getSlot(0).getItem()
                    : ItemStack.EMPTY;
            boolean has_retired = UmaSoulUtils.getGrowth(soul) == Growth.RETIRED;
            boolean has_learned = UmaSoulUtils.getSkills(soul)
                    .contains(StringTag.valueOf(skill.getRegistryName().toString()));
            boolean has_learned_upper = skill.getUpperSkill() != null && 
                    UmaSoulUtils.getSkills(soul).contains(StringTag.valueOf(skill.getUpperSkill().toString()));
            boolean slot_needed = !soul.isEmpty() && !UmaSoulUtils.hasEmptySkillSlot(soul);
            if (has_learned || has_learned_upper)
                guiGraphics.drawString(this.font, Component.translatable("umapyoi.skill.has_learned_skill"), 51, 31, 0x794016, false);
            else if (has_retired)
                guiGraphics.drawString(this.font, Component.translatable("umapyoi.skill.has_retired"), 51, 31, 0x794016, false);
            else if (slot_needed)
                guiGraphics.drawString(this.font, Component.translatable("umapyoi.skill.slot_needed"), 51, 31, 0x794016, false);
            else if (skill.getRequiredWisdom() > 0)
                guiGraphics.drawString(this.font, Component.translatable("umapyoi.skill.require_wisdom",
                        UmaStatusUtils.getStatusLevel(skill.getRequiredWisdom())), 51, 31, 0x794016, false);
            else
                guiGraphics.drawString(this.font, Component.translatable("umapyoi.skill.no_require"), 51, 31, 0x794016, false);
        }

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pX, int pY) {
        super.renderBg(guiGraphics, pPartialTick, pX, pY);
        UmaSkill skill = this.getBookSkill();
        if (skill != null) {
            int i = (this.width - this.imageWidth) / 2;
            int j = (this.height - this.imageHeight) / 2;
            switch (skill.getType()) {
            case BUFF -> guiGraphics.blit(RenderType::guiOpaqueTexturedBackground, BACKGROUND_TEXTURE, i + 31, j + 21, 176, 21, 16, 16, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
            case HINDER -> guiGraphics.blit(RenderType::guiOpaqueTexturedBackground, BACKGROUND_TEXTURE, i + 31, j + 21, 176, 37, 16, 16, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
            case HEAL -> guiGraphics.blit(RenderType::guiOpaqueTexturedBackground, BACKGROUND_TEXTURE, i + 31, j + 21, 176, 53, 16, 16, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
            case PASSIVE -> guiGraphics.blit(RenderType::guiOpaqueTexturedBackground, BACKGROUND_TEXTURE, i + 31, j + 21, 176, 69, 16, 16, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
            default -> throw new IllegalArgumentException("Unexpected value: " + skill.getType());
            }
        }
    }

    @Override
    protected void renderErrorIcon(GuiGraphics guiGraphics, int x, int y) {
    }

    private UmaSkill getBookSkill() {
        ItemStack book = this.getMenu().getSlot(1).hasItem() ? this.getMenu().getSlot(1).getItem() : ItemStack.EMPTY;
        if (book.getItem()instanceof SkillBookItem skillBook) {
            UmaSkill skill = skillBook.getSkill(book);
            return skill;
        }
        return null;
    }
}

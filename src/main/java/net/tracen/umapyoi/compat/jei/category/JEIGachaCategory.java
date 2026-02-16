package net.tracen.umapyoi.compat.jei.category;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.compat.jei.JEIPlugin;
import net.tracen.umapyoi.compat.jei.recipes.JEISimpleRecipe;
import net.tracen.umapyoi.item.ItemRegistry;

import java.util.Optional;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;

public class JEIGachaCategory implements IRecipeCategory<JEISimpleRecipe> {
    private final Component title;
    private final Identifier UID;
    private final IDrawable background;
    private final IDrawable chancedSlot;
    private final IDrawable icon;

    public JEIGachaCategory(IGuiHelper helper) {
        title = Component.translatable("umapyoi.jei.gacha");
        Identifier backgroundImage = Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/gui/jei_compat.png");
        UID = Identifier.fromNamespaceAndPath(Umapyoi.MODID, "gacha");
        background = helper.createDrawable(backgroundImage, 0, 0, 93, 46);
        chancedSlot = helper.createDrawable(backgroundImage, 93, 0, 18, 18);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(BlockRegistry.UMA_PEDESTAL));
    }
    
    @Override
    public IRecipeType<JEISimpleRecipe> getRecipeType() {
        return JEIPlugin.GACHA_JEI_TYPE;
    }

    @Override
    public void draw(JEISimpleRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX,
                     double mouseY) {
        background.draw(guiGraphics);
        Optional<ItemStack> outputStack = recipeSlotsView.findSlotByName("outputSlot")
                .flatMap(slot -> slot.getDisplayedIngredient(VanillaTypes.ITEM_STACK));
        outputStack.ifPresent(output -> {
            if (output.is(ItemRegistry.SUPPORT_CARD)) {
                Minecraft minecraft = Minecraft.getInstance();
                Font font = minecraft.font;
                var needBookText = Component.translatable("umapyoi.jei.gacha.need_book");
                guiGraphics.drawString(font, needBookText,
                        46 - Math.round(font.width(needBookText.getVisualOrderText()) / 2.0F), 36, 0xFFFEFEFE);
                //RenderSystem.setShaderColor(1, 1, 1, 1);
            }
        });
        if (recipe.getOutputs().size() > 1)
            chancedSlot.draw(guiGraphics, 62, 14);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JEISimpleRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 14, 15).setSlotName("inputSlot").addItemStacks(recipe.getInputs());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 63, 15).setSlotName("outputSlot")
                .addItemStacks(recipe.getOutputs());
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public int getWidth() {
        return background.getWidth();
    }

    @Override
    public int getHeight() {
        return background.getHeight();
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

}

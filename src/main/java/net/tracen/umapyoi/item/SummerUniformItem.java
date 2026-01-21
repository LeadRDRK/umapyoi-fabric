package net.tracen.umapyoi.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.utils.ClientUtils;

public class SummerUniformItem extends AbstractSuitItem {
    @Override
    protected ResourceLocation getModel(ItemStack stack) {
        return ClientUtils.SUMMER_UNIFORM;
    }

    @Override
    protected ResourceLocation getTexture(ItemStack stack, boolean tanned) {
        return tanned ? ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/summer_uniform_tanned.png")
                : ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/summer_uniform.png");
    }

    @Override
    protected ResourceLocation getFlatModel(ItemStack stack) {
        return ClientUtils.SUMMER_UNIFORM_FLAT;
    }

    @Override
    protected ResourceLocation getFlatTexture(ItemStack stack, boolean tanned) {
        return tanned ? ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/summer_uniform_tanned.png")
                : ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/summer_uniform.png");
    }
}

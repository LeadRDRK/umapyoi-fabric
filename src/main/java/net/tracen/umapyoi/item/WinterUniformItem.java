package net.tracen.umapyoi.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.utils.ClientUtils;

public class WinterUniformItem extends AbstractSuitItem {
    @Override
    public ResourceLocation getModel(ItemStack stack) {
        return ClientUtils.WINTER_UNIFORM;
    }

    @Override
    public ResourceLocation getTexture(ItemStack stack, boolean tanned) {
        return tanned ? new ResourceLocation(Umapyoi.MODID, "textures/model/winter_uniform_tanned.png")
                : new ResourceLocation(Umapyoi.MODID, "textures/model/winter_uniform.png");
    }

    @Override
    public ResourceLocation getFlatModel(ItemStack stack) {
        return ClientUtils.WINTER_UNIFORM_FLAT;
    }

    @Override
    public ResourceLocation getFlatTexture(ItemStack stack, boolean tanned) {
        return tanned ? new ResourceLocation(Umapyoi.MODID, "textures/model/winter_uniform_tanned.png")
                : new ResourceLocation(Umapyoi.MODID, "textures/model/winter_uniform.png");
    }
}

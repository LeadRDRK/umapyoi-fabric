package net.tracen.umapyoi.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.utils.ClientUtils;

public class KindergartenUniformItem extends AbstractSuitItem {
    @Override
    protected ResourceLocation getModel(ItemStack stack) {
        return ClientUtils.KINDERGARTEN_UNIFORM;
    }

    @Override
    protected ResourceLocation getTexture(ItemStack stack, boolean tanned) {
        return new ResourceLocation(Umapyoi.MODID, "textures/model/kindergarten_uniform.png");
    }

    @Override
    protected ResourceLocation getFlatModel(ItemStack stack) {
        return ClientUtils.KINDERGARTEN_UNIFORM;
    }

    @Override
    protected ResourceLocation getFlatTexture(ItemStack stack, boolean tanned) {
        return new ResourceLocation(Umapyoi.MODID, "textures/model/kindergarten_uniform.png");
    }
}

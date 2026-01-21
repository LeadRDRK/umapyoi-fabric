package net.tracen.umapyoi.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.utils.ClientUtils;

public class SwimsuitItem extends AbstractSuitItem {
    @Override
    protected ResourceLocation getModel(ItemStack stack) {
        return ClientUtils.SWIMSUIT;
    }

    @Override
    protected ResourceLocation getTexture(ItemStack stack, boolean tanned) {
        return tanned ? ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/swimsuit_tanned.png")
                : ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/swimsuit.png");
    }

    @Override
    protected ResourceLocation getFlatModel(ItemStack stack) {
        return ClientUtils.SWIMSUIT_FLAT;
    }

    @Override
    protected ResourceLocation getFlatTexture(ItemStack stack, boolean tanned) {
        return tanned ? ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/swimsuit_flat_tanned.png")
                : ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/swimsuit_flat.png");
    }
}

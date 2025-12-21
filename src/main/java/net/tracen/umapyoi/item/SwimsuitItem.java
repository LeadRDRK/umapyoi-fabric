package net.tracen.umapyoi.item;

import net.minecraft.resources.ResourceLocation;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.utils.ClientUtils;

public class SwimsuitItem extends AbstractSuitItem {
    @Override
    protected ResourceLocation getModel() {
        return ClientUtils.SWIMSUIT;
    }

    @Override
    protected ResourceLocation getTexture(boolean tanned) {
        return tanned ? new ResourceLocation(Umapyoi.MODID, "textures/model/swimsuit_tanned.png")
                : new ResourceLocation(Umapyoi.MODID, "textures/model/swimsuit.png");
    }

    @Override
    protected ResourceLocation getFlatModel() {
        return ClientUtils.SWIMSUIT_FLAT;
    }

    @Override
    protected ResourceLocation getFlatTexture(boolean tanned) {
        return tanned ? new ResourceLocation(Umapyoi.MODID, "textures/model/swimsuit_flat_tanned.png")
                : new ResourceLocation(Umapyoi.MODID, "textures/model/swimsuit_flat.png");
    }
}

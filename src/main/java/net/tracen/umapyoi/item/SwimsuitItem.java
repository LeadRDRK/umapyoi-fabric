package net.tracen.umapyoi.item;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.utils.ClientUtils;

public class SwimsuitItem extends AbstractSuitItem {
    public SwimsuitItem(Properties p) {
        super(p);
    }

    @Override
    public Identifier getModel(ItemStack stack) {
        return ClientUtils.SWIMSUIT;
    }

    @Override
    public Identifier getTexture(ItemStack stack, boolean tanned) {
        return tanned ? Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/swimsuit_tanned.png")
                : Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/swimsuit.png");
    }

    @Override
    public Identifier getFlatModel(ItemStack stack) {
        return ClientUtils.SWIMSUIT_FLAT;
    }

    @Override
    public Identifier getFlatTexture(ItemStack stack, boolean tanned) {
        return tanned ? Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/swimsuit_flat_tanned.png")
                : Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/swimsuit_flat.png");
    }
}

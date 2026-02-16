package net.tracen.umapyoi.item;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.utils.ClientUtils;

public class WinterUniformItem extends AbstractSuitItem {
    public WinterUniformItem(Properties p) {
        super(p);
    }

    @Override
    protected Identifier getModel(ItemStack stack) {
        return ClientUtils.WINTER_UNIFORM;
    }

    @Override
    protected Identifier getTexture(ItemStack stack, boolean tanned) {
        return tanned ? Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/winter_uniform_tanned.png")
                : Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/winter_uniform.png");
    }

    @Override
    protected Identifier getFlatModel(ItemStack stack) {
        return ClientUtils.WINTER_UNIFORM_FLAT;
    }

    @Override
    protected Identifier getFlatTexture(ItemStack stack, boolean tanned) {
        return tanned ? Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/winter_uniform_tanned.png")
                : Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/winter_uniform.png");
    }
}

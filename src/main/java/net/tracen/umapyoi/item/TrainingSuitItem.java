package net.tracen.umapyoi.item;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.utils.ClientUtils;

public class TrainingSuitItem extends AbstractSuitItem {
    public TrainingSuitItem(Properties p) {
        super(p);
    }

    @Override
    protected Identifier getModel(ItemStack stack) {
        return ClientUtils.TRAINING_SUIT;
    }

    @Override
    protected Identifier getTexture(ItemStack stack, boolean tanned) {
        return tanned ? Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/trainning_suit_tanned.png")
                : Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/trainning_suit.png");
    }

    @Override
    protected Identifier getFlatModel(ItemStack stack) {
        return ClientUtils.TRAINING_SUIT_FLAT;
    }

    @Override
    protected Identifier getFlatTexture(ItemStack stack, boolean tanned) {
        return tanned ? Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/trainning_suit_tanned.png")
                : Identifier.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/trainning_suit.png");
    }
}

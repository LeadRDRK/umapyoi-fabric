package net.tracen.umapyoi.client.model;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.item.UmaCostumeItem;

import org.jetbrains.annotations.Nullable;

public class UmaCostumeItemModel extends DynamicItemBakedModel {
    public UmaCostumeItemModel(ItemModel original) {
        super(original);
    }

    @Override
    public ItemModel resolveModel(ItemModel original, ItemStack stack, @Nullable ClientLevel level,
                                  @Nullable ItemOwner owner, int seed) {
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof UmaCostumeItem) {
                var costumeId = UmaCostumeItem.getCostumeID(stack);
                return getModel(costumeId);
            }
        }
        return this.getOriginalModel();
    }
}

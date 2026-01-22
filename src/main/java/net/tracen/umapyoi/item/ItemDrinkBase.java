package net.tracen.umapyoi.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.tracen.umapyoi.item.info.FoodInfo;

public class ItemDrinkBase extends ItemFoodBase {

    public ItemDrinkBase(Item.Properties prop, FoodInfo info) {
        super(prop, info);
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    @Override
    public FoodInfo getFoodInfo() {
        return super.getFoodInfo();
    }
}
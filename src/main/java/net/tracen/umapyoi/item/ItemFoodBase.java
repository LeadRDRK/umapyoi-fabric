package net.tracen.umapyoi.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.item.info.FoodInfo;

public class ItemFoodBase extends Item implements IFoodLike {
    private final FoodInfo info;

    public ItemFoodBase(Item.Properties prop, FoodInfo info) {
        super(prop.food(buildProperties(info)));
        this.info = info;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack itemstack = super.finishUsingItem(stack, level, entity);
        if (stack.getCount() > 0) {
            if (entity instanceof Player) {
                Player entityplayer = (Player) entity;
                if (entityplayer.getAbilities().instabuild)
                    return itemstack;
                if (!entityplayer.addItem(this.getRecipeRemainder(stack)))
                    entityplayer.drop(this.getRecipeRemainder(stack), true);
            }
            return itemstack;
        }
        return entity instanceof Player && ((Player) entity).getAbilities().instabuild ? itemstack
                : this.getRecipeRemainder(stack);
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return super.getDrinkingSound();
    }

    @Override
    public SoundEvent getEatingSound() {
        return super.getEatingSound();
    }

    @Override
    public FoodInfo getFoodInfo() {
        return info;
    }

    public static FoodProperties buildProperties(FoodInfo info) {
        FoodProperties.Builder food = new FoodProperties.Builder().nutrition(info.getAmount())
                .saturationModifier(info.getCalories());
        if (info.isAlwaysEat())
            food.alwaysEdible();
        if (info.getEatTime() <= 16)
            food.fast();
        info.getEffects().forEach((k) -> food.effect(k.getFirst().get(), k.getSecond()));

        return food.build();
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        if (this.getFoodInfo() != null)
            return this.getFoodInfo().getEatTime();
        return super.getUseDuration(stack);
    }

    @Override
    public boolean shouldAddEffectTooltips() {
        return this.info != null;
    }

}
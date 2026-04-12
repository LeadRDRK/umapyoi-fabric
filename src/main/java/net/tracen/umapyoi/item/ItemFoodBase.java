package net.tracen.umapyoi.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.item.info.FoodInfo;

public class ItemFoodBase extends Item implements IFoodLike {
    private final FoodInfo info;

    public ItemFoodBase(Item.Properties prop, FoodInfo info) {
        super(prop.food(buildProperties(info))
                .component(DataComponents.CONSUMABLE, buildConsumable(info)));
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
                var remainder = this.getCraftingRemainder(stack).create();
                if (!entityplayer.addItem(remainder))
                    entityplayer.drop(remainder, true);
            }
            return itemstack;
        }
        return entity instanceof Player && ((Player) entity).getAbilities().instabuild ? itemstack
                : this.getCraftingRemainder(stack).create();
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

        return food.build();
    }

    public static Consumable buildConsumable(FoodInfo info) {
        var food = Consumable.builder();
        if (info.getEatTime() <= 16) {
            food.consumeSeconds(0f);
            food.animation(ItemUseAnimation.NONE);
        }
        info.getEffects().forEach((k) ->
                food.onConsume(new ApplyStatusEffectsConsumeEffect(k.getFirst().get(), k.getSecond())));

        return food.build();
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        if (this.getFoodInfo() != null)
            return this.getFoodInfo().getEatTime();
        return super.getUseDuration(stack, entity);
    }

    @Override
    public boolean shouldAddEffectTooltips() {
        return this.info != null;
    }

}
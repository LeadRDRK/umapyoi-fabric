package net.tracen.umapyoi.item.food;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.item.ItemDrinkBase;
import net.tracen.umapyoi.item.info.FoodInfo;

import java.util.function.Consumer;

public class UmaDrinkItem extends ItemDrinkBase {
    private final Consumer<LivingEntity> consumer;

    public UmaDrinkItem(Item.Properties properties, Consumer<LivingEntity> consumer, FoodInfo info) {
        super(properties, info);
        this.consumer = consumer;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack itemstack = stack.has(DataComponents.FOOD) ? this.eatAsUma(stack, level, entity) : stack;
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

    private ItemStack eatAsUma(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (UmapyoiAPI.getUmaSoul(player).isEmpty()) {
                var info = this.getFoodInfo();
                player.getFoodData().eat(info.getAmount(), info.getCalories());
                if (!player.getAbilities().instabuild)
                    stack.shrink(1);
                return stack;
            } else {
                this.consumer.accept(player);
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

}

package net.tracen.umapyoi.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CommonResultSlot extends Slot {
    private final Player player;
    private int removeCount;

    public CommonResultSlot(Player player, Container container, int index, int xPosition, int yPosition) {
        super(container, index, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }

    @Override
    public void onTake(Player thePlayer, ItemStack stack) {
        this.checkTakeAchievements(stack);
        super.onTake(thePlayer, stack);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        stack.onCraftedBy(this.player, this.removeCount);
        this.removeCount = 0;
    }
}

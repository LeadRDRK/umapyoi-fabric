package net.tracen.umapyoi.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.tracen.umapyoi.events.AnvilUpdateCallback;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    public AnvilMenuMixin() {
        super(null, 0, null, null, null);
    }

    @Accessor("itemName")
    abstract String getItemName();

    @Accessor("cost")
    abstract DataSlot getCost_();

    @Accessor("repairItemCountCost")
    abstract void setRepairItemCountCost(int value);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 1), method = "createResult", cancellable = true)
    public void createResult(CallbackInfo callback) {
        var left = inputSlots.getItem(0);
        var right = inputSlots.getItem(1);
        var baseCost = left.getOrDefault(DataComponents.REPAIR_COST, 0)
                + (right.isEmpty() ? 0 : right.getOrDefault(DataComponents.REPAIR_COST, 0));

        var result = AnvilUpdateCallback.invoke(left, right, getItemName(), baseCost, player);
        if (result.cancel)
            callback.cancel();

        if (!result.output.isEmpty()) {
            resultSlots.setItem(0, result.output);
            getCost_().set(result.cost);
            setRepairItemCountCost(result.materialCost);
            callback.cancel();
        }
    }
}

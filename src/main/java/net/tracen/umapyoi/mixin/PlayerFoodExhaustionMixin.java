package net.tracen.umapyoi.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.registry.umadata.Growth;
import net.tracen.umapyoi.utils.UmaSoulUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils.StatusType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, priority = 10)
public class PlayerFoodExhaustionMixin {
    // Correct coordinates by Mixin method
    @Inject(method = "causeFoodExhaustion", at = @At(value = "HEAD"), cancellable = true)
    private void foodExhaustion(float pExhaustion, CallbackInfo ci) {
        Player player = (Player)(Object) this;
        ItemStack umaSoul = UmapyoiAPI.getUmaSoul(player);
        if (!umaSoul.isEmpty()) {
            if (!player.getAbilities().invulnerable) {
                if (!player.level().isClientSide()) {
                    float exhaustionMultipler = 1.2F - this.getExactProperty(umaSoul, StatusType.STAMINA, 0.85F);
                    player.getFoodData().addExhaustion(pExhaustion * exhaustionMultipler);
                }
            }
            ci.cancel();
        }
    }

    private float getExactProperty(ItemStack stack, StatusType type, double limit) {
        var retiredValue = UmaSoulUtils.getGrowth(stack) == Growth.RETIRED ? 1.0D : 0.25D;
        var totalProperty = propertyPercentage(stack, type);
        return (float) (UmaSoulUtils.getMotivation(stack).getMultiplier() * limit * retiredValue * totalProperty);
    }

    private double propertyPercentage(ItemStack stack, StatusType type) {
        int x = UmaSoulUtils.getProperty(stack).get(type);
        var statLimit = Umapyoi.CONFIG.STAT_LIMIT_VALUE();
        var denominator = 1 + Math.pow(Math.E,
                (x > statLimit ? (-0.125 * Umapyoi.CONFIG.STAT_LIMIT_REDUCTION_RATE()) : -0.125) *
                        (x - statLimit));
        return 1 / denominator;
    }
}

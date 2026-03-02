package net.tracen.umapyoi.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.events.LivingEntityUseItemEvents;
import net.tracen.umapyoi.registry.UmapyoiAttributesRegistry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = LivingEntity.class, priority = 500)
public abstract class LivingEntityMixin {
    @ModifyReturnValue(method = "createLivingAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder addExtraAttributes(AttributeSupplier.Builder builder) {
        return builder
                .add(UmapyoiAttributesRegistry.SPRINT_SPEED)
                .add(UmapyoiAttributesRegistry.STEP_HEIGHT_ADDITION)
                .add(UmapyoiAttributesRegistry.SWIM_SPEED);
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V", ordinal = 0))
    private float swimSpeed(float origSpeed) {
        return origSpeed * (float)(((LivingEntity)(Object) this)
                .getAttribute(UmapyoiAttributesRegistry.SWIM_SPEED))
                .getValue();
    }

    @ModifyArg(method = "jumpInLiquid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"), index = 1)
    private double modifySwimSpeed(double y) {
        return y * ((LivingEntity)(Object) this)
                .getAttribute(UmapyoiAttributesRegistry.SWIM_SPEED)
                .getValue();
    }

    @ModifyReturnValue(method = "maxUpStep", at = @At("RETURN"))
    private float modifyStepHeight(float origStep) {
        return (float) (origStep + ((LivingEntity)(Object) this)
                .getAttribute(UmapyoiAttributesRegistry.STEP_HEIGHT_ADDITION)
                .getValue());
    }

    @Shadow
    public abstract ItemStack getUseItem();

    @Inject(method = "completeUsingItem", at = @At(value = "INVOKE", shift = At.Shift.BY, by = 2, target = "Lnet/minecraft/world/item/ItemStack;finishUsingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void onFinishUsing(CallbackInfo ci, InteractionHand hand, ItemStack result) {
        LivingEntityUseItemEvents.FINISH.invoker().onUseItem((LivingEntity) (Object) this, this.getUseItem().copy());
    }
}
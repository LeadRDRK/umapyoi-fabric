package net.tracen.umapyoi.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.tracen.umapyoi.registry.UmapyoiAttributesRegistry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LivingEntity.class, priority = 500)
public abstract class LivingEntityMixin {
    @ModifyReturnValue(method = "createLivingAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder addExtraAttributes(AttributeSupplier.Builder builder) {
        return builder
                .add(UmapyoiAttributesRegistry.SPRINT_SPEED)
                .add(UmapyoiAttributesRegistry.STEP_HEIGHT_ADDITION);
    }

    @ModifyReturnValue(method = "maxUpStep", at = @At("RETURN"))
    private float modifyStepHeight(float origStep) {
        return (float) (origStep + ((LivingEntity)(Object) this)
                .getAttribute(UmapyoiAttributesRegistry.STEP_HEIGHT_ADDITION)
                .getValue());
    }
}
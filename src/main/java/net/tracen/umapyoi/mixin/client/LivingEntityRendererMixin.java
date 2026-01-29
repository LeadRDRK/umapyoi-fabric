package net.tracen.umapyoi.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.tracen.umapyoi.api.UmapyoiAPI;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V")
    private void preExtractRenderState(LivingEntity entity, LivingEntityRenderState state, float f,
                                       CallbackInfo info) {
        state.umapyoi$setUmaSoul(UmapyoiAPI.getRenderingUmaSoul(entity));
        if (state.umapyoi$getEarTailAnimationOffset() == -1)
            state.umapyoi$setEarTailAnimationOffset((int)Math.abs(entity.getUUID().getLeastSignificantBits()) % 10);
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getRenderType(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/renderer/RenderType;")
    private void getRenderType(LivingEntityRenderState state, boolean isVisible, boolean renderTranslucent,
                               boolean appearsGlowing, CallbackInfoReturnable<RenderType> ci) {
        if (!state.umapyoi$getUmaSoul().isEmpty()) {
            ci.setReturnValue(null);
        }
    }
}

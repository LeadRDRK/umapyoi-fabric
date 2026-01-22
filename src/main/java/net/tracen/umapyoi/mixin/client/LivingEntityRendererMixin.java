package net.tracen.umapyoi.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.tracen.umapyoi.events.client.RenderPlayerCallback;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    public static PlayerRenderState lastPlayerRenderState = null;

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private void preRender(LivingEntityRenderState state, PoseStack poseStack,
                           MultiBufferSource multiBufferSource, int packedLight,
                           CallbackInfo info) {
        if (!((Object)this instanceof PlayerRenderer renderer)
                || !(state instanceof PlayerRenderState playerRenderState))
            return;

        lastPlayerRenderState = playerRenderState;
        RenderPlayerCallback.Pre.invoke(new RenderPlayerCallback.Context(
                renderer, EntityRenderDispatcherMixin.lastClientPlayer, playerRenderState,
                poseStack, multiBufferSource, packedLight));
    }

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private void postRender(LivingEntityRenderState state, PoseStack poseStack,
                            MultiBufferSource multiBufferSource, int packedLight,
                            CallbackInfo info) {
        if (!((Object)this instanceof PlayerRenderer renderer)
                || !(state instanceof PlayerRenderState playerRenderState))
            return;

        RenderPlayerCallback.Post.invoke(new RenderPlayerCallback.Context(
                renderer, EntityRenderDispatcherMixin.lastClientPlayer, playerRenderState,
                poseStack, multiBufferSource, packedLight));
    }
}

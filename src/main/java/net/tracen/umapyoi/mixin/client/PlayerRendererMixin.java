package net.tracen.umapyoi.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.tracen.umapyoi.events.client.RenderArmCallback;
import net.tracen.umapyoi.events.client.RenderPlayerCallback;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", ordinal = 0), method = "render")
    private void preRender(AbstractClientPlayer entity, float entityYaw, float partialTicks,
                           PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                           CallbackInfo info) {
        RenderPlayerCallback.Pre.invoke(new RenderPlayerCallback.Context(
                (PlayerRenderer)(Object) this, entity, entityYaw, partialTicks, poseStack, buffer, packedLight));
    }

    @Inject(at = @At("TAIL"), method = "render")
    private void postRender(AbstractClientPlayer entity, float entityYaw, float partialTicks,
                           PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                           CallbackInfo info) {
        RenderPlayerCallback.Post.invoke(new RenderPlayerCallback.Context(
                (PlayerRenderer)(Object) this, entity, entityYaw, partialTicks, poseStack, buffer, packedLight));
    }

    @Inject(at = @At("HEAD"), method = "renderRightHand", cancellable = true)
    private void renderRightHand(PoseStack poseStack, MultiBufferSource buffer, int combinedLight,
                                 AbstractClientPlayer player, CallbackInfo info) {
        if (RenderArmCallback.invoke(new RenderArmCallback.Context(
                poseStack, buffer, combinedLight, player, HumanoidArm.RIGHT)))
            info.cancel();
    }

    @Inject(at = @At("HEAD"), method = "renderLeftHand", cancellable = true)
    private void renderLeftHand(PoseStack poseStack, MultiBufferSource buffer, int combinedLight,
                                AbstractClientPlayer player, CallbackInfo info) {
        if (RenderArmCallback.invoke(new RenderArmCallback.Context(
                poseStack, buffer, combinedLight, player, HumanoidArm.LEFT)))
            info.cancel();
    }
}

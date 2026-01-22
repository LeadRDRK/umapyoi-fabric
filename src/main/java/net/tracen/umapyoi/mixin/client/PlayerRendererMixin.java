package net.tracen.umapyoi.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.tracen.umapyoi.events.client.RenderArmCallback;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerRenderState, PlayerModel> {
    public PlayerRendererMixin() {
        super(null, null, 0.0F);
    }

    @Inject(at = @At("HEAD"), method = "renderRightHand", cancellable = true)
    private void renderRightHand(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                 ResourceLocation skinTexture, boolean isSleeveVisible,
                                 CallbackInfo info) {
        if (RenderArmCallback.invoke(new RenderArmCallback.Context(
                EntityRenderDispatcherMixin.lastClientPlayer, LivingEntityRendererMixin.lastPlayerRenderState,
                poseStack, bufferSource, packedLight, skinTexture, isSleeveVisible, HumanoidArm.RIGHT)))
            info.cancel();
    }

    @Inject(at = @At("HEAD"), method = "renderLeftHand", cancellable = true)
    private void renderLeftHand(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                ResourceLocation skinTexture, boolean isSleeveVisible,
                                CallbackInfo info) {
        if (RenderArmCallback.invoke(new RenderArmCallback.Context(
                EntityRenderDispatcherMixin.lastClientPlayer, LivingEntityRendererMixin.lastPlayerRenderState,
                poseStack, bufferSource, packedLight, skinTexture, isSleeveVisible, HumanoidArm.LEFT)))
            info.cancel();
    }
}

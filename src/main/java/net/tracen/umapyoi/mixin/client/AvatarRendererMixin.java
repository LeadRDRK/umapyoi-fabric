package net.tracen.umapyoi.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.HumanoidArm;
import net.tracen.umapyoi.client.renderer.ItemInHandRendererMixinState;
import net.tracen.umapyoi.events.client.RenderArmCallback;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity>
        extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {
    public AvatarRendererMixin() {
        super(null, null, 0.0F);
    }

    @Inject(at = @At("HEAD"), method = "renderRightHand", cancellable = true)
    private void renderRightHand(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight,
                                 Identifier skinTexture, boolean renderSleeve,
                                 CallbackInfo info) {
        var player = Objects.requireNonNull(ItemInHandRendererMixinState.player);
        if (RenderArmCallback.invoke(new RenderArmCallback.Context(player, poseStack, nodeCollector,
                packedLight, skinTexture, renderSleeve, HumanoidArm.RIGHT)))
            info.cancel();
    }

    @Inject(at = @At("HEAD"), method = "renderLeftHand", cancellable = true)
    private void renderLeftHand(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight,
                                Identifier skinTexture, boolean renderSleeve,
                                CallbackInfo info) {
        var player = Objects.requireNonNull(ItemInHandRendererMixinState.player);
        if (RenderArmCallback.invoke(new RenderArmCallback.Context(player, poseStack, nodeCollector,
                packedLight, skinTexture, renderSleeve, HumanoidArm.LEFT)))
            info.cancel();
    }
}

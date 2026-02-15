package net.tracen.umapyoi.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = PlayerItemInHandLayer.class, priority = 10)
public class PlayerItemInHandLayerMixin {
    @Inject(method = "submitArmWithItem", at = @At(value = "HEAD"))
    private void submitArmWithItemHead(AvatarRenderState avatarRenderState,
                                       ItemStackRenderState itemStackRenderState,
                                       HumanoidArm humanoidArm, PoseStack poseStack,
                                       SubmitNodeCollector submitNodeCollector, int i,
                                       CallbackInfo ci) {
        if (!avatarRenderState.umapyoi$getUmaSoul().isEmpty()) {
            boolean leftArmFlag = humanoidArm == HumanoidArm.LEFT;
            boolean slimArmFlag = false;
            // 1 / 16 = 0.0625D, right arm direction is the X positive direction
            var layer = (PlayerItemInHandLayer<?, ?>) (Object) this;
            if (layer.getParentModel() instanceof PlayerModel playerModel)
                if (playerModel.slim)
                    slimArmFlag = true;
            poseStack.translate((slimArmFlag ? 0.5 : 1) * (leftArmFlag ? -0.125D : 0.0625D), 0D, 0D);
        }
    }
}

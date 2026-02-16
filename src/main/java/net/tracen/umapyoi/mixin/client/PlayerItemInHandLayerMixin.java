package net.tracen.umapyoi.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = PlayerItemInHandLayer.class, priority = 10)
public class PlayerItemInHandLayerMixin {
    @Inject(at = @At(value = "HEAD"), method = "Lnet/minecraft/client/renderer/entity/layers/PlayerItemInHandLayer;submitArmWithItem(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V")
    private void submitArmWithItemHead(AvatarRenderState avatarRenderState,
                                       ItemStackRenderState itemStackRenderState,
                                       ItemStack itemStack, HumanoidArm humanoidArm,
                                       PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
                                       int i,
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

package net.tracen.umapyoi.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.api.UmapyoiAPI;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = PlayerItemInHandLayer.class, priority = 10)
public class ItemInHandLayerMixin {
    // Correct coordinates by Mixin method
    @Inject(method = "renderArmWithItem", at = @At(value = "HEAD"))
    private void renderArmWithItemHead(LivingEntity entity, ItemStack stack, ItemDisplayContext display, HumanoidArm arm, PoseStack poseStack, MultiBufferSource source, int light, CallbackInfo ci) {
        if (!UmapyoiAPI.getUmaSoul(entity).isEmpty() && UmapyoiAPI.isUmaSoulRendering(entity)) {
            boolean leftArmFlag = arm == HumanoidArm.LEFT;
            boolean slimArmFlag = false;
            //  1 / 16 = 0.0625D, right arm direction is the X positive direction
            PlayerItemInHandLayer<?,?> layer = (PlayerItemInHandLayer<?,?>)(Object) this;
            if(layer.getParentModel() instanceof PlayerModel<?> playerModel)
                if(((PlayerModelMixin<?>) playerModel).isSlim()) slimArmFlag = true;
            poseStack.translate((slimArmFlag ? 0.5 : 1) * (leftArmFlag ? -0.125D : 0.0625D), 0D, 0D);
        }
    }
}

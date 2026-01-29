package net.tracen.umapyoi.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.client.renderer.ItemInHandRendererMixinState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private void renderArmWithItemHead(AbstractClientPlayer player, float partialTicks, float pitch,
                                   InteractionHand hand, float swingProgress, ItemStack stack,
                                   float equippedProgress, PoseStack poseStack, MultiBufferSource buffer,
                                   int combinedLight, CallbackInfo ci) {
        ItemInHandRendererMixinState.player = player;
    }
}

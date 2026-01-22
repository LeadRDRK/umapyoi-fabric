package net.tracen.umapyoi.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    public static AbstractClientPlayer lastClientPlayer = null;

    @Inject(method = "render", at = @At("HEAD"))
    private <E extends Entity, S extends EntityRenderState> void render(
            E entity, double xOffset, double yOffset, double zOffset, float partialTick,
            PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
            EntityRenderer<? super E, S> renderer, CallbackInfo info) {
        if (entity instanceof AbstractClientPlayer clientPlayer) {
            lastClientPlayer = clientPlayer;
        }
    }
}

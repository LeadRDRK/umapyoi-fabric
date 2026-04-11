package net.tracen.umapyoi.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.tracen.umapyoi.events.PlayerSpawnPhantomsCallback;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;blockPosition()Lnet/minecraft/core/BlockPos;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void onBeforeSpawn(ServerLevel level, boolean spawnEnemies, CallbackInfo ci,
                               RandomSource z, Iterator<?> zz, ServerPlayer serverPlayer) {
        PlayerSpawnPhantomsCallback.invoke(serverPlayer);
    }
}

package net.tracen.umapyoi.mixin;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.storage.loot.LootTable;

// TODO: not needed on fabric api for 1.21.2+
@Mixin(GiveGiftToHero.class)
public class GiveGiftToHeroMixin {
    @Shadow
    @Final
    @Mutable
    private static Map<VillagerProfession, ResourceKey<LootTable>> GIFTS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void makeMutable(CallbackInfo ci) {
        GIFTS = new HashMap<>(GIFTS);
    }
}
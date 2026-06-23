package net.tracen.umapyoi.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.data.tag.UmapyoiItemTags;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(value = HumanoidMobRenderer.class)
public class HumanoidMobRendererMixin {
    @Inject(at = @At("RETURN"), cancellable = true, method = "Lnet/minecraft/client/renderer/entity/HumanoidMobRenderer;getEquipmentIfRenderable(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;")
    private static void getEquipmentIfRenderable(LivingEntity entity, EquipmentSlot slot,
                                                 CallbackInfoReturnable<ItemStack> ci) {
        ItemStack equipment;
        if (slot.getType() != EquipmentSlot.Type.HAND && !(equipment = ci.getReturnValue()).isEmpty()) {
            var umasoul = UmapyoiAPI.getRenderingUmaSoul(entity);
            if (!Umapyoi.CONFIG.VANILLA_ARMOR_RENDER && !umasoul.isEmpty()) {
                boolean renderElytra = Umapyoi.CONFIG.ELYTRA_RENDER && equipment.getItem() == Items.ELYTRA;
                boolean shouldRender = equipment.is(UmapyoiItemTags.SHOULD_RENDER);
                if (!renderElytra && !shouldRender)
                    ci.setReturnValue(ItemStack.EMPTY);
            }
        }
    }
}

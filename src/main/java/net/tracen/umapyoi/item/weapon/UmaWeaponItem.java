package net.tracen.umapyoi.item.weapon;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tracen.umapyoi.api.UmapyoiAPI;

public class UmaWeaponItem extends Item {
    public UmaWeaponItem(ToolMaterial material, int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties) {
        super(material.applySwordProperties(pProperties, (float) pAttackDamageModifier, pAttackSpeedModifier));
    }

    @Override
    public void inventoryTick(ItemStack pStack, ServerLevel pLevel, Entity pEntity, EquipmentSlot pSlot) {

        super.inventoryTick(pStack, pLevel, pEntity, pSlot);
        if (pSlot == null)
            return;

        if (pEntity instanceof LivingEntity living) {
            ItemStack soul = UmapyoiAPI.getUmaSoul(living);
            if(soul.isEmpty()) {
                living.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 100, 1));
                living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 1));
            }
        }
    }

    /**
     * Current implementations of this method in child classes do not use the entry
     * argument beside ev. They just raise the damage on the stack.
     */
    @Override
    public void hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, EquipmentSlot.MAINHAND);
    }

    /**
     * Called when a {@link net.minecraft.world.level.block.Block} is destroyed
     * using this Item. Return {@code true} to trigger the "Use Item" statistic.
     */
    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos,
                             LivingEntity pEntityLiving) {
        if (pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            pStack.hurtAndBreak(2, pEntityLiving, EquipmentSlot.MAINHAND);
        }

        return true;
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
        if (enchantment.is(Enchantments.VANISHING_CURSE)) return true;
        return super.canBeEnchantedWith(stack, enchantment, context);
    }
}

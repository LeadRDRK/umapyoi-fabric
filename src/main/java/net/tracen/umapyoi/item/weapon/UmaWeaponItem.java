package net.tracen.umapyoi.item.weapon;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tracen.umapyoi.api.UmapyoiAPI;

public class UmaWeaponItem extends TieredItem {
    public UmaWeaponItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties) {
        super(pTier, pProperties
                .component(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                        .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier",
                                (float) pAttackDamageModifier + pTier.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier",
                                pAttackSpeedModifier, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)
                        .build()));
    }

    public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        return !pPlayer.isCreative();
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {

        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (pStack == null)
            return;
        if (pEntity == null)
            return;
        if(!pIsSelected)
            return;

        if (pEntity instanceof LivingEntity living) {
            ItemStack soul = UmapyoiAPI.getUmaSoul(living);
            if(soul.isEmpty()) {
                living.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 1));
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
            }
        }
    }

    /**
     * Current implementations of this method in child classes do not use the entry
     * argument beside ev. They just raise the damage on the stack.
     */
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, EquipmentSlot.MAINHAND);
        return true;
    }

    /**
     * Called when a {@link net.minecraft.world.level.block.Block} is destroyed
     * using this Item. Return {@code true} to trigger the "Use Item" statistic.
     */
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos,
                             LivingEntity pEntityLiving) {
        if (pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            pStack.hurtAndBreak(2, pEntityLiving, EquipmentSlot.MAINHAND);
        }

        return true;
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, Enchantment enchantment, EnchantingContext context) {
        if (enchantment == Enchantments.VANISHING_CURSE) return true;
        return super.canBeEnchantedWith(stack, enchantment, context);
    }
}

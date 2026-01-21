package net.tracen.umapyoi.item.weapon;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.tracen.umapyoi.Umapyoi;

public class BaseballBatItem extends UmaWeaponItem {
    private static final ResourceLocation KNOCKBACK_ID = ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "knockback");

    public BaseballBatItem() {
        super(new NaginataTier(), 6, -2.7F, Umapyoi.defaultItemProperties()
                .stacksTo(1)
                .attributes(createAttributes()));
    }

    private static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_KNOCKBACK,
                        new AttributeModifier(KNOCKBACK_ID, 3D,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    private static class NaginataTier implements Tier {

        @Override
        public int getUses() {
            return 2250;
        }

        @Override
        public float getSpeed() {
            return 0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 1F;
        }

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
        }

        @Override
        public int getEnchantmentValue() {
            return 20;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ConventionalItemTags.STORAGE_BLOCKS_IRON);
        }

    }
}

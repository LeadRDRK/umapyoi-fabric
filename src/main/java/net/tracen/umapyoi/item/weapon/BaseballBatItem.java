package net.tracen.umapyoi.item.weapon;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.tracen.umapyoi.Umapyoi;

public class BaseballBatItem extends UmaWeaponItem {
    private static final Identifier KNOCKBACK_ID = Identifier.fromNamespaceAndPath(Umapyoi.MODID, "knockback");

    public BaseballBatItem(Properties p) {
        super(createMaterial(), 6, -2.7F, p);
    }

    public static Properties createProperties() {
        return Umapyoi.defaultItemProperties()
                .stacksTo(1)
                .attributes(createAttributes());
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

    private static ToolMaterial createMaterial() {
        return new ToolMaterial(
                BlockTags.INCORRECT_FOR_WOODEN_TOOL, // incorrectBlocksForDrops
                2250, // durability
                0F, // speed
                1F, // attackDamageBonus
                20, // enchantmentValue
                ConventionalItemTags.STORAGE_BLOCKS_IRON // repairItems
        );
    }
}

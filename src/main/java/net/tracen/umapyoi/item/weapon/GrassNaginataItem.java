package net.tracen.umapyoi.item.weapon;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.data.tag.UmapyoiItemTags;

public class GrassNaginataItem extends UmaWeaponItem {
    private static final ResourceLocation REACH_ID = ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "reach");

    public GrassNaginataItem(Properties p) {
        super(createMaterial(), 7, -2.7F, p);
    }

    public static Properties createProperties() {
        return Umapyoi.defaultItemProperties()
                .stacksTo(1)
                .attributes(createAttributes());
    }

    private static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(REACH_ID, 2D,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    private static ToolMaterial createMaterial() {
        return new ToolMaterial(
                BlockTags.INCORRECT_FOR_WOODEN_TOOL, // incorrectBlocksForDrops
                1561, // durability
                0F, // speed
                1F, // attackDamageBonus
                20, // enchantmentValue
                UmapyoiItemTags.HORSESHOE_RAINBOW // repairItems
        );
    }
}
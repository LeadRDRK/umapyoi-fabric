package net.tracen.umapyoi.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.tracen.umapyoi.Umapyoi;

public class UmapyoiAttributesRegistry {
    public static final Attribute SPRINT_SPEED = new RangedAttribute(
            "attribute.umapyoi.generic.sprint_speed",
            0.7F, 0.0D, 1024.0D).setSyncable(true);

    public static final Attribute STEP_HEIGHT_ADDITION = new RangedAttribute(
            "attribute.umapyoi.generic.step_height_addition",
            0.0D, -512.0D, 512.0D).setSyncable(true);

    public static final Attribute SWIM_SPEED = new RangedAttribute(
            "attribute.umapyoi.generic.swim_speed",
            1.0D, 0.0D, 1024.0D).setSyncable(true);

    public static void register() {
        register("sprint_speed", SPRINT_SPEED);
        register("step_height_addition", STEP_HEIGHT_ADDITION);
        register("swim_speed", SWIM_SPEED);
    }

    private static void register(String path, Attribute attribute) {
        Registry.register(BuiltInRegistries.ATTRIBUTE, new ResourceLocation(Umapyoi.MODID, path), attribute);
    }
}

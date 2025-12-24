package net.tracen.umapyoi.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.tracen.umapyoi.Umapyoi;

public class UmapyoiAttributesRegistry {
    public static final LazyRegistrar<Attribute> ATTRIBUTES = LazyRegistrar.create(
            BuiltInRegistries.ATTRIBUTE.key(),
            Umapyoi.MODID);

    public static final RegistryObject<Attribute> SPRINT_SPEED = ATTRIBUTES.register("sprint_speed",
            () -> new RangedAttribute("attribute.umapyoi.generic.sprint_speed", (double)0.7F, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistryObject<Attribute> STEP_HEIGHT_ADDITION = ATTRIBUTES.register("step_height_addition",
            () -> new RangedAttribute("attribute.umapyoi.generic.step_height_addition", 0.0D, -512.0D, 512.0D).setSyncable(true));

    public static final RegistryObject<Attribute> SWIM_SPEED = ATTRIBUTES.register("swim_speed",
            () -> new RangedAttribute("attribute.umapyoi.generic.swim_speed", 1.0D, 0.0D, 1024.0D).setSyncable(true));
}

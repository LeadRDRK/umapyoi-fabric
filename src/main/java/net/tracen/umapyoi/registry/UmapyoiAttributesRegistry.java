package net.tracen.umapyoi.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.tracen.umapyoi.Umapyoi;

public class UmapyoiAttributesRegistry {
    public static final Holder<Attribute> SPRINT_SPEED = register("sprint_speed",
            "attribute.umapyoi.generic.sprint_speed",
            0.7D, 0.0D, 1024.0D, true);

    public static final Holder<Attribute> STEP_HEIGHT_ADDITION = register("step_height_addition",
            "attribute.umapyoi.generic.step_height_addition",
            0.0D, -512.0D, 512.0D, true);

    private static Holder<Attribute> register(
            String path, String descriptionId, double defaultValue, double minValue, double maxValue, boolean syncedWithClient
    ) {
        var name = Identifier.fromNamespaceAndPath(Umapyoi.MODID, path);
        Attribute entityAttribute = new RangedAttribute(
                descriptionId,
                defaultValue,
                minValue,
                maxValue
        ).setSyncable(syncedWithClient);

        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, name, entityAttribute);
    }

    public static void register() {
        // dummy
    }
}

package net.tracen.umapyoi.data.tag;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.registry.cosmetics.CosmeticData;

public class UmapyoiCostumeDataTags {

    public static final TagKey<CosmeticData> HAT_HIDEHAIR = UmapyoiCostumeDataTags.umapyoiCosmeticDataTag("hat/hide_hair");

    public static TagKey<CosmeticData> umapyoiCosmeticDataTag(String path) {
        return TagKey.create(CosmeticData.REGISTRY_KEY, Identifier.fromNamespaceAndPath(Umapyoi.MODID, path));
    }

    public static TagKey<CosmeticData> modCosmeticDataTag(String modid, String path) {
        return TagKey.create(CosmeticData.REGISTRY_KEY, Identifier.fromNamespaceAndPath(modid, path));
    }
}

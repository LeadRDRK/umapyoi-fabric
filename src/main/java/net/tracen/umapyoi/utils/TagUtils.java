package net.tracen.umapyoi.utils;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class TagUtils {
    public static TagKey<Item> modItemTag(String modid, String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(modid, path));
    }

    public static TagKey<Block> modBlockTag(String modid, String path) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(modid, path));
    }

    public static TagKey<EntityType<?>> modEntityTag(String modid, String path) {
        return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(modid, path));
    }

    public static TagKey<Fluid> modFluidTag(String modid, String path) {
        return TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath(modid, path));
    }

    public static TagKey<Item> cItemTag(String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", path));
    }
}
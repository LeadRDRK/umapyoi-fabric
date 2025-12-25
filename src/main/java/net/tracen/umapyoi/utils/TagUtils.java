package net.tracen.umapyoi.utils;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class TagUtils {
    public static TagKey<Item> modItemTag(String modid, String path) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(modid, path));
    }

    public static TagKey<Block> modBlockTag(String modid, String path) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(modid, path));
    }

    public static TagKey<EntityType<?>> modEntityTag(String modid, String path) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(modid, path));
    }

    public static TagKey<Fluid> modFluidTag(String modid, String path) {
        return TagKey.create(Registries.FLUID, new ResourceLocation(modid, path));
    }

    public static TagKey<Item> cItemTag(String path) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("c", path));
    }
}
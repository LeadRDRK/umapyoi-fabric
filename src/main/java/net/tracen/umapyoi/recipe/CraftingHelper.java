package net.tracen.umapyoi.recipe;

import com.google.gson.JsonSyntaxException;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Objects;

public class CraftingHelper {
    public static Item getItem(String itemName, boolean disallowsAirInRecipe) {
        ResourceLocation itemKey = ResourceLocation.parse(itemName);
        if (!BuiltInRegistries.ITEM.containsKey(itemKey))
            throw new JsonSyntaxException("Unknown item '" + itemName + "'");

        Item item = BuiltInRegistries.ITEM.get(itemKey);
        if (disallowsAirInRecipe && item == Items.AIR)
            throw new JsonSyntaxException("Invalid item: " + itemName);
        return Objects.requireNonNull(item);
    }
}

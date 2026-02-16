package net.tracen.umapyoi.block.entity;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public interface Gachable {
    public Predicate<? super Identifier> getFilter(Level level, ItemStack input);
}

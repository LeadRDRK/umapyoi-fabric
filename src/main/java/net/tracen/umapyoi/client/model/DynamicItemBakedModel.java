package net.tracen.umapyoi.client.model;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

/** Ported from MMLib **/
public abstract class DynamicItemBakedModel implements ItemModel {
    private final ItemModel original;

    public DynamicItemBakedModel(ItemModel original) {
        this.original = original;
    }

    public ItemModel getOriginalModel() {
        return this.original;
    }

    public abstract ItemModel resolveModel(ItemModel original, ItemStack stack,
                                           @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed);

    @Override
    @ParametersAreNonnullByDefault
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver itemModelResolver,
                       ItemDisplayContext displayContext, @Nullable ClientLevel level,
                       @Nullable LivingEntity entity, int seed) {
        var model = resolveModel(original, stack, level, entity, seed);
        model.update(renderState, stack, itemModelResolver, displayContext, level, entity, seed);
    }
}
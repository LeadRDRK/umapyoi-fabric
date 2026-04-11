package net.tracen.umapyoi.client.model;

import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.MissingItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

/** Ported from MMLib **/
public abstract class DynamicItemBakedModel implements ItemModel {
    public static Map<Identifier, ExtraModelKey<ItemModel>> MODELS = new HashMap<>();

    private final ItemModel original;

    public DynamicItemBakedModel(ItemModel original) {
        this.original = original;
    }

    public ItemModel getOriginalModel() {
        return this.original;
    }

    public abstract ItemModel resolveModel(ItemModel original, ItemStack stack,
                                           @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed);

    @Override
    @ParametersAreNonnullByDefault
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver itemModelResolver,
                       ItemDisplayContext displayContext, @Nullable ClientLevel level,
                       @Nullable ItemOwner owner, int seed) {
        var model = resolveModel(original, stack, level, owner, seed);
        if (model != null) {
            model.update(renderState, stack, itemModelResolver, displayContext, level, owner, seed);
        }
    }

    protected ItemModel getModel(Identifier id) {
        var key = MODELS.get(id);
        if (key == null)
            return this.getOriginalModel();

        ItemModel model = Minecraft.getInstance().getModelManager().getModel(key);
        if(model instanceof MissingItemModel)
            return this.getOriginalModel();

        return model;
    }
}
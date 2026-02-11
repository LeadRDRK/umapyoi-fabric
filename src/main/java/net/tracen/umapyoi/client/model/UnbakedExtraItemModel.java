package net.tracen.umapyoi.client.model;

import net.fabricmc.fabric.api.client.model.loading.v1.UnbakedExtraModel;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;

public class UnbakedExtraItemModel implements UnbakedExtraModel<ItemModel> {
    private final ResourceLocation model;

    public UnbakedExtraItemModel(ResourceLocation model) {
        this.model = model;
    }

    @Override
    public ItemModel bake(ModelBaker baker) {
        var resolvedModel = baker.getModel(model);
        var textureSlots = resolvedModel.getTopTextureSlots();
        var quads = resolvedModel.bakeTopGeometry(textureSlots, baker, BlockModelRotation.X0_Y0).getAll();
        var properties = ModelRenderProperties.fromResolvedModel(baker, resolvedModel, textureSlots);
        return new BlockModelWrapper(Collections.emptyList(), quads, properties);
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        resolver.markDependency(model);
    }
}

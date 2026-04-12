package net.tracen.umapyoi.client.model;

import net.fabricmc.fabric.api.client.model.loading.v1.UnbakedExtraModel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.resources.Identifier;

import java.util.Collections;

public class UnbakedExtraItemModel implements UnbakedExtraModel<ItemModel> {
    private final Identifier model;

    public UnbakedExtraItemModel(Identifier model) {
        this.model = model;
    }

    @Override
    public ItemModel bake(ModelBaker baker) {
        var resolvedModel = baker.getModel(model);
        var textureSlots = resolvedModel.getTopTextureSlots();
        var quads = resolvedModel.bakeTopGeometry(textureSlots, baker, BlockModelRotation.IDENTITY);
        var properties = ModelRenderProperties.fromResolvedModel(baker, resolvedModel, textureSlots);
        return new CuboidItemModelWrapper(Collections.emptyList(), quads, properties, ModelState.NO_TRANSFORM);
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        resolver.markDependency(model);
    }
}

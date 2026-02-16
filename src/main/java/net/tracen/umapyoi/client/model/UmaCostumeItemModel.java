package net.tracen.umapyoi.client.model;

import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.MissingItemModel;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.UmaCostumeItem;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class UmaCostumeItemModel extends DynamicItemBakedModel {
    private static Map<Identifier, ExtraModelKey<ItemModel>> models = new HashMap<>();

    public UmaCostumeItemModel(ItemModel original) {
        super(original);
    }

    @Override
    public ItemModel resolveModel(ItemModel original, ItemStack stack, @Nullable ClientLevel level,
                                  @Nullable ItemOwner owner, int seed) {
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof UmaCostumeItem) {
                var costumeId = UmaCostumeItem.getCostumeID(stack);
                var key = models.get(costumeId);
                if (key == null)
                    return this.getOriginalModel();

                ItemModel model = Minecraft.getInstance().getModelManager().getModel(key);
                if(model instanceof MissingItemModel)
                    return this.getOriginalModel();

                return model;
            }
        }
        return this.getOriginalModel();
    }

    public static void onModelLoading(ModelLoadingPlugin.Context pluginContext) {
        models.clear();
        FileToIdConverter.json("models/item/costume")
                .listMatchingResources(Minecraft.getInstance().getResourceManager())
                .keySet()
                .stream()
                .map(location -> {
                    Umapyoi.getLogger().info("Found resource:{}", location.toString());
                    var path = location.getPath();
                    return Identifier.fromNamespaceAndPath(location.getNamespace(),
                            path.substring("models/".length(), path.length() - ".json".length()));
                })
                .forEach(location -> {
                    var model = new UnbakedExtraItemModel(location);
                    var costumeId = Identifier.fromNamespaceAndPath(
                            location.getNamespace(),
                            location.getPath().substring("item/costume/".length())
                    );
                    var key = ExtraModelKey.<ItemModel>create(location::toString);

                    pluginContext.addModel(key, model);
                    models.put(costumeId, key);
                });
    }
}

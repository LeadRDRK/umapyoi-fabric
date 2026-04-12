package net.tracen.umapyoi.mixin.client;

import net.fabricmc.fabric.api.client.model.loading.v1.UnbakedModelDeserializer;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ClientItemInfoLoader;
import net.minecraft.client.resources.model.ModelDiscovery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.Identifier;
import net.minecraft.util.profiling.Zone;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.events.handler.ClientSetupEvents;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.Map;

@Mixin(ModelManager.class)
public class ModelManagerMixin {
    @Inject(
            method = "discoverModelDependencies",
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/resources/model/ModelDiscovery;addSpecialModel(Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/resources/model/UnbakedModel;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void insertExtraModels(
            Map<Identifier, UnbakedModel> allModels,
            BlockStateModelLoader.LoadedModels blockStateModels,
            ClientItemInfoLoader.LoadedClientInfos itemInfos,
            CallbackInfoReturnable cir,
            Zone z,
            ModelDiscovery result
    ) {
        ClientSetupEvents.forEachDynamicItemModelResource((location, resource) -> {
            try {
                var reader = resource.openAsReader();
                var model = UnbakedModelDeserializer.deserialize(reader);
                result.modelWrappers.put(location, result.createAndQueueWrapper(location, model));
            } catch (IOException e) {
                Umapyoi.getLogger().error("Error while loading {}: {}", location, e);
            }
        });
    }
}

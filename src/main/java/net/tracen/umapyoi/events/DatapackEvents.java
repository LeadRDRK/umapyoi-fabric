package net.tracen.umapyoi.events;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.tracen.umapyoi.recipe.UmasoulIngredient;
import net.tracen.umapyoi.registry.cosmetics.CosmeticData;
import net.tracen.umapyoi.registry.races.Race;
import net.tracen.umapyoi.registry.races.field.RaceField;
import net.tracen.umapyoi.registry.races.tags.RaceTag;
import net.tracen.umapyoi.registry.training.card.SupportCard;
import net.tracen.umapyoi.registry.umadata.UmaData;

public class DatapackEvents {
    public static void registerDatapackRegistries() {
        DynamicRegistries.registerSynced(UmaData.REGISTRY_KEY, UmaData.CODEC);
        DynamicRegistries.registerSynced(SupportCard.REGISTRY_KEY, SupportCard.CODEC);
        DynamicRegistries.registerSynced(CosmeticData.REGISTRY_KEY, CosmeticData.CODEC);
        DynamicRegistries.registerSynced(Race.REGISTRY_KEY, Race.CODEC);
        DynamicRegistries.registerSynced(RaceField.REGISTRY_KEY, RaceField.CODEC);
        DynamicRegistries.registerSynced(RaceTag.REGISTRY_KEY, RaceTag.CODEC);
    }

    public static void registerSerializers() {
        CustomIngredientSerializer.register(UmasoulIngredient.Serializer.INSTANCE);
    }
}

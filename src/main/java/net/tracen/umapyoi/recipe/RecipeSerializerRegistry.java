package net.tracen.umapyoi.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.registry.LazyRegistrar;
import net.tracen.umapyoi.registry.RegistryObject;

public class RecipeSerializerRegistry {
    public static final LazyRegistrar<RecipeSerializer<?>> RECIPE_SERIALIZER = LazyRegistrar
            .create(Registries.RECIPE_SERIALIZER, Umapyoi.MODID);

    public static final RegistryObject<RecipeSerializer<?>> SHAPED_UMASOUL = RECIPE_SERIALIZER
            .register("shaped_umasoul", () -> ShapedUmasoulRecipe.SERIALIZER);

    public static final RegistryObject<RecipeSerializer<?>> SHAPELESS_UMASOUL = RECIPE_SERIALIZER
            .register("shapeless_umasoul", () -> ShapelessUmasoulRecipe.SERIALIZER);

    public static final RegistryObject<RecipeSerializer<?>> SHAPED_CARD = RECIPE_SERIALIZER
            .register("shaped_card", () -> ShapedSupportCardRecipe.SERIALIZER);

    public static final RegistryObject<RecipeSerializer<?>> SHAPELESS_CARD = RECIPE_SERIALIZER
            .register("shapeless_card", () -> ShapelessSupportCardRecipe.SERIALIZER);

    public static final RegistryObject<RecipeSerializer<?>> SHAPED_COSTUME = RECIPE_SERIALIZER
            .register("shaped_costume", () -> ShapedCostumeRecipe.SERIALIZER);

    public static final RegistryObject<RecipeSerializer<?>> SHAPELESS_COSTUME = RECIPE_SERIALIZER
            .register("shapeless_costume", () -> ShapelessCostumeRecipe.SERIALIZER);

    public static final RegistryObject<RecipeSerializer<?>> SHAPELESS_RACE_TICKET = RECIPE_SERIALIZER
            .register("shapeless_race_ticket", () -> ShapelessRaceTicketRecipe.SERIALIZER);
}

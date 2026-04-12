package net.tracen.umapyoi.recipe;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;

import java.util.Optional;

public class ShapedCostumeRecipe extends ShapedRecipe {

    public static final RecipeSerializer<ShapedRecipe> SERIALIZER = new CostumeRecipeSerializerFactory<>(
            ShapedRecipe.SERIALIZER, ShapedCostumeRecipe::new
    ).createAsCompose();

    private final Identifier output;

    public ShapedCostumeRecipe(ShapedRecipe compose, Identifier outputBlade) {
        super(
                new CommonInfo(compose.showNotification()),
                new CraftingBookInfo(compose.category(), compose.group()),
                new ShapedRecipePattern(compose.getWidth(), compose.getHeight(), compose.getIngredients(), Optional.empty()),
                getResultItem(outputBlade)
        );
        this.output = outputBlade;
    }

    private static ItemStackTemplate getResultItem(Identifier output) {
        if (BuiltInRegistries.ITEM.containsKey(output)) {
            return new ItemStackTemplate(BuiltInRegistries.ITEM.get(output).orElseThrow().value());
        }
        else {
            return new ItemStackTemplate(ItemRegistry.UMA_COSTUME,
                    DataComponentPatch.builder()
                            .set(DataComponentsTypeRegistry.DATA_LOCATION.get(), output)
                            .build());
        }
    }

    public Identifier getOutput() {
        return output;
    }

    @Override
    public RecipeSerializer<ShapedRecipe> getSerializer() {
        return SERIALIZER;
    }

}

package net.tracen.umapyoi.recipe;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;

public class ShapelessUmasoulRecipe extends ShapelessRecipe {

    public static final RecipeSerializer<ShapelessRecipe> SERIALIZER = new UmasoulRecipeSerializerFactory<>(
            ShapelessRecipe.SERIALIZER, ShapelessUmasoulRecipe::new
    ).createAsCompose();

    private final Identifier outputUma;

    public ShapelessUmasoulRecipe(ShapelessRecipe compose, Identifier output) {
        super(
                new CommonInfo(compose.showNotification()),
                new CraftingBookInfo(compose.category(), compose.group()),
                getResultItem(output),
                compose.ingredients
        );
        this.outputUma = output;
    }

    private static ItemStackTemplate getResultItem(Identifier output) {
        if (BuiltInRegistries.ITEM.containsKey(output)) {
            return new ItemStackTemplate(BuiltInRegistries.ITEM.get(output).orElseThrow().value());
        }
        else {
            return new ItemStackTemplate(ItemRegistry.BLANK_UMA_SOUL,
                    DataComponentPatch.builder()
                            .set(DataComponentsTypeRegistry.DATA_LOCATION.get(), output)
                            .build());
        }
    }

    public Identifier getOutputUma() {
        return outputUma;
    }

    @Override
    public RecipeSerializer<ShapelessRecipe> getSerializer() {
        return SERIALIZER;
    }

}

package net.tracen.umapyoi.recipe;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;

public class ShapelessSupportCardRecipe extends ShapelessRecipe {

    public static final RecipeSerializer<ShapelessRecipe> SERIALIZER = new SupportCardRecipeSerializerFactory<>(
            ShapelessRecipe.SERIALIZER, ShapelessSupportCardRecipe::new
    ).createAsCompose();

    private final Identifier outputUma;

    public ShapelessSupportCardRecipe(ShapelessRecipe compose, Identifier outputBlade) {
        super(
                new CommonInfo(compose.showNotification()),
                new CraftingBookInfo(compose.category(), compose.group()),
                getResultItem(outputBlade),
                compose.ingredients
        );
        this.outputUma = outputBlade;
    }

    private static ItemStackTemplate getResultItem(Identifier output) {
        if (BuiltInRegistries.ITEM.containsKey(output)) {
            return new ItemStackTemplate(BuiltInRegistries.ITEM.get(output).orElseThrow().value());
        }
        else {
            return new ItemStackTemplate(ItemRegistry.SUPPORT_CARD,
                    DataComponentPatch.builder()
                            .set(DataComponentsTypeRegistry.DATA_LOCATION.get(), output)
                            .build());
        }
    }

    public Identifier getOutput() {
        return outputUma;
    }

    @Override
    public RecipeSerializer<ShapelessRecipe> getSerializer() {
        return SERIALIZER;
    }

}

package net.tracen.umapyoi.recipe;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.KeyCompressor;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapEncoder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public record CostumeRecipeSerializerFactory<T extends Recipe<?>, U extends T> (
        RecipeSerializer<T> compose,
        BiFunction<T, @Nullable Identifier, U> converter
) {
    public RecipeSerializer<U> create() {
        return new RecipeSerializer<>(codec(), streamCodec());
    }

    @SuppressWarnings("unchecked")
    public RecipeSerializer<T> createAsCompose() {
        return (RecipeSerializer<T>) create();
    }

    @NullMarked
    public MapCodec<U> codec() {
        return MapCodec.of(
                new MapEncoder<>() {
                    @Override
                    public <V> Stream<V> keys(DynamicOps<V> ops) {
                        throw notImplemented();
                    }

                    @Override
                    public <V> RecordBuilder<V> encode(U input, DynamicOps<V> ops, RecordBuilder<V> prefix) {
                        throw notImplemented();
                    }

                    @Override
                    public <V> KeyCompressor<V> compressor(DynamicOps<V> ops) {
                        throw notImplemented();
                    }

                    private NotImplementedException notImplemented() {
                        return new NotImplementedException("Serializing CostumeRecipe is not implemented yet.");
                    }
                },
                new MapDecoder<>() {
                    @Override
                    public <V> Stream<V> keys(DynamicOps<V> ops) {
                        return Stream.concat(
                                compose().codec().keys(ops),
                                Stream.of(ops.createString("cosmetic"))
                        );
                    }

                    @Override
                    public <V> DataResult<U> decode(DynamicOps<V> ops, MapLike<V> input) {
                        if (input == null) {
                            return DataResult.error(() -> "Input is null");
                        }

                        MapLike<V> newInput;
                        if (input.get("result") == null) {
                            var newMap = ops.mergeToMap(
                                    ops.empty(),
                                    input
                            ).flatMap(map ->
                                    ops.mergeToMap(map, ops.createString("result"), ops.createMap(
                                            Stream.of(Pair.of(
                                                    ops.createString("id"),
                                                    ops.createString("umapyoi:support_card")
                                            ))
                                    ))
                            ).getOrThrow();

                            newInput = ops.getMap(newMap).getOrThrow();
                        }
                        else {
                            newInput = input;
                        }
                        var resultField = newInput.get("result");

                        var recipeResult = compose().codec().decode(ops, newInput);
                        return recipeResult.flatMap(recipe -> {
                            var outputResult = Identifier.CODEC.optionalFieldOf("cosmetic").decode(ops, newInput);
                            return outputResult.map(outputOpt -> {
                                var output = outputOpt
                                        .orElseGet(() -> // result.id MUST be present for the base recipe to even decode correctly
                                                Identifier.CODEC.fieldOf("id").codec()
                                                        .decode(ops, resultField)
                                                        .result()
                                                        .orElseThrow()
                                                        .getFirst()
                                        );

                                return converter.apply(recipe, output);
                            });
                        });
                    }

                    @Override
                    public <V> KeyCompressor<V> compressor(DynamicOps<V> ops) {
                        return new KeyCompressor<>(ops, keys(ops));
                    }
                }
        );
    }

    public StreamCodec<RegistryFriendlyByteBuf, U> streamCodec() {
        return StreamCodec.composite(
                compose().streamCodec(), recipe -> recipe,
                Identifier.STREAM_CODEC.apply(ByteBufCodecs::optional), recipe -> {
                    if (recipe instanceof ShapedCostumeRecipe bladeRecipe) {
                        return Optional.ofNullable(bladeRecipe.getOutput());
                    } else if (recipe instanceof ShapelessCostumeRecipe bladeRecipe) {
                        return Optional.ofNullable(bladeRecipe.getOutput());
                    } else
                        return Optional.empty();
                },
                (recipe, output) -> converter.apply(recipe, output.orElse(null))
        );
    }
}
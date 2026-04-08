package net.tracen.umapyoi.recipe;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.tracen.umapyoi.item.ItemRegistry;

import java.util.function.BiFunction;
import java.util.stream.Stream;

public class RaceTicketRecipeSerializer<T extends Recipe<?>, U extends T> implements RecipeSerializer<U> {
    private final MapCodec<U> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, U> streamCodec;

    public RaceTicketRecipeSerializer(RecipeSerializer<T> compose, BiFunction<T, ResourceLocation, U> converter) {
        this.codec = makeCodec(compose, converter);
        this.streamCodec = makeStreamCodec(compose, converter);
    }

    private static <T extends Recipe<?>, U extends T> MapCodec<U> makeCodec(RecipeSerializer<T> compose,
                                                                            BiFunction<T, ResourceLocation, U> converter) {
        var mapCodec = compose.codec();
        return new MapCodec<>() {
            @Override
            public <V> RecordBuilder<V> encode(U input, DynamicOps<V> ops, RecordBuilder<V> prefix) {
                var race = ResourceLocation.CODEC
                        .encodeStart(ops, ((RaceTicketRecipe<?>) input).getKey());
                return mapCodec.encode(input, ops, prefix)
                        .add(ops.createString("race"), race);
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
                                            ops.createString(BuiltInRegistries.ITEM.getKey(ItemRegistry.UMA_RACE_TICKET).toString())
                                    ))
                            ))
                    ).getOrThrow();

                    newInput = ops.getMap(newMap).getOrThrow();
                }
                else {
                    newInput = input;
                }
                var resultField = newInput.get("result");

                var recipeResult = mapCodec.decode(ops, newInput);
                return recipeResult.flatMap(recipe -> {
                    var outputResult = ResourceLocation.CODEC.optionalFieldOf("race").decode(ops, newInput);
                    return outputResult.map(outputOpt -> {
                        var output = outputOpt
                                .orElseGet(() -> // result.id MUST be present for the base recipe to even decode correctly
                                        ResourceLocation.CODEC.fieldOf("id").codec()
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
            public <V> Stream<V> keys(DynamicOps<V> ops) {
                return Stream.concat(
                        mapCodec.keys(ops),
                        Stream.of(ops.createString("race"))
                );
            }
        };
    }

    private static <T extends Recipe<?>, U extends T>
    StreamCodec<RegistryFriendlyByteBuf, U> makeStreamCodec(RecipeSerializer<T> compose,
                                                            BiFunction<T, ResourceLocation, U> converter) {
        return StreamCodec.composite(
                compose.streamCodec(), recipe -> recipe,
                ResourceLocation.STREAM_CODEC, recipe -> ((RaceTicketRecipe<?>) recipe).getKey(),
                converter
        );
    }

    @Override
    @MethodsReturnNonnullByDefault
    public MapCodec<U> codec() {
        return codec;
    }

    @Override
    @MethodsReturnNonnullByDefault
    public StreamCodec<RegistryFriendlyByteBuf, U> streamCodec() {
        return streamCodec;
    }
}
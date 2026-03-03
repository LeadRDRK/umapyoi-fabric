package net.tracen.umapyoi.recipe;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.tracen.umapyoi.item.ItemRegistry;

import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.stream.Stream;

public class RaceTicketRecipeSerializer<T extends Recipe<?>, U extends T> implements RecipeSerializer<U> {
    private final RecipeSerializer<T> compose;
    private final BiFunction<T, ResourceLocation, U> converter;
    private final Codec<U> codec;

    public RaceTicketRecipeSerializer(RecipeSerializer<T> compose, BiFunction<T, ResourceLocation, U> converter) {
        this.compose = compose;
        this.converter = converter;
        this.codec = makeCodec(compose, converter);
    }

    private static <T extends Recipe<?>, U extends T> Codec<U> makeCodec(RecipeSerializer<T> compose,
                                                                         BiFunction<T, ResourceLocation, U> converter) {
        if (compose.codec() instanceof MapCodec.MapCodecCodec<T> mapCodecCodec) {
            var mapCodec = mapCodecCodec.codec();
            return new MapCodec<U>() {
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
                                                ops.createString("item"),
                                                ops.createString(ItemRegistry.UMA_RACE_TICKET.getId().toString())
                                        ))
                                ))
                        ).getOrThrow(false, e -> {});

                        newInput = ops.getMap(newMap).getOrThrow(false, e -> {});
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
                                    .orElseGet(() -> // result.item MUST be present for the base recipe to even decode correctly
                                            ResourceLocation.CODEC.fieldOf("item").codec()
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
            }.codec();
        }
        else {
            throw new UnsupportedOperationException("Composite codec is not a MapCodec");
        }
    }

    @Override
    @MethodsReturnNonnullByDefault
    public Codec<U> codec() {
        return codec;
    }

    @Override
    public @Nullable U fromNetwork(FriendlyByteBuf pBuffer) {
        T recipe = compose.fromNetwork(pBuffer);
        if (pBuffer.readBoolean())
            return converter.apply(recipe, pBuffer.readResourceLocation());
        return converter.apply(recipe, null);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, U pRecipe) {
        compose.toNetwork(pBuffer, pRecipe);
        if (pRecipe instanceof ShapelessRaceTicketRecipe shapeless) {
            boolean hasName = shapeless.getKey() != null;
            pBuffer.writeBoolean(hasName);
            if (hasName) pBuffer.writeResourceLocation(shapeless.getKey());
        } else {
            pBuffer.writeBoolean(false);
        }
    }
}
package net.tracen.umapyoi.recipe;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.stream.Stream;

public record UmasoulRecipeSerializer<T extends Recipe<?>, U extends T> (RecipeSerializer<T> compose,
                                                                         BiFunction<T, @Nullable ResourceLocation, U> converter) implements RecipeSerializer<U> {
    @Override
    @MethodsReturnNonnullByDefault
    public Codec<U> codec() {
        return Codec.of(
                new Encoder<>() {
                    @Override
                    public <V> DataResult<V> encode(U input, DynamicOps<V> ops, V prefix) {
                        throw new NotImplementedException("Serializing UmasoulRecipe is not implemented yet.");
                    }
                },
                new Decoder<>() {
                    @Override
                    public <V> DataResult<Pair<U, V>> decode(DynamicOps<V> ops, V input) {
                        if (input == null) {
                            return DataResult.error(() -> "Input is null");
                        }

                        V newInput;
                        if (ops.get(input, "result").result().isEmpty()) {
                            newInput = ops.mergeToMap(input, ops.createString("result"), ops.createMap(
                                    Stream.of(Pair.of(
                                            ops.createString("item"),
                                            ops.createString("umapyoi:blank_uma_soul")
                                    ))
                            )).result().orElse(input);
                        }
                        else {
                            newInput = input;
                        }
                        var resultField = ops.get(newInput, "result").result();

                        var baseResult = compose().codec().decode(ops, newInput);
                        return baseResult.flatMap(basePair -> {
                            var extraResult = ResourceLocation.CODEC.optionalFieldOf("umasoul").codec().decode(ops, newInput);
                            return extraResult.map(extraPair -> {
                                var outputUma = extraPair.getFirst()
                                        .orElseGet(() -> // result.item MUST be present for the base recipe to even decode correctly
                                                ResourceLocation.CODEC.fieldOf("item").codec()
                                                        .decode(ops, resultField.orElseThrow())
                                                        .result()
                                                        .orElseThrow()
                                                        .getFirst()
                                        );

                                return Pair.of(converter.apply(basePair.getFirst(), outputUma), newInput);
                            });
                        });
                    }
                }
        );
    }

    @Override
    @NotNull
    public U fromNetwork(@NotNull FriendlyByteBuf buf) {
        T recipe = compose().fromNetwork(buf);
        if (buf.readBoolean())
            return converter().apply(recipe, buf.readResourceLocation());
        return converter().apply(recipe, null);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull U recipe) {
        compose().toNetwork(buf, recipe);
        if (recipe instanceof ShapedUmasoulRecipe umaRecipe) {
            boolean hasName = umaRecipe.getOutputUma() != null;
            buf.writeBoolean(hasName);
            if (hasName)
                buf.writeResourceLocation(umaRecipe.getOutputUma());
        } else if (recipe instanceof ShapelessUmasoulRecipe umaRecipe) {
            boolean hasName = umaRecipe.getOutputUma() != null;
            buf.writeBoolean(hasName);
            if (hasName)
                buf.writeResourceLocation(umaRecipe.getOutputUma());
        }else
            buf.writeBoolean(false);
    }
}
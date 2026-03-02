package net.tracen.umapyoi.recipe;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.tracen.umapyoi.item.ItemRegistry;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.stream.Stream;

public record RaceTicketRecipeSerializer<T extends Recipe<?>, U extends T> (RecipeSerializer<T> compose,
                                                                            BiFunction<T, ResourceLocation, U> converter)
        implements RecipeSerializer<U> {

    @Override
    @MethodsReturnNonnullByDefault
    public Codec<U> codec() {
        return Codec.of(
                new Encoder<>() {
                    @Override
                    public <V> DataResult<V> encode(U input, DynamicOps<V> ops, V prefix) {
                        throw new NotImplementedException("Serializing RaceTicketRecipe is not implemented yet.");
                    }
                },
                new Decoder<>() {
                    @Override
                    public <V> DataResult<Pair<U, V>> decode(DynamicOps<V> ops, V input) {
                        V newInput;
                        if (ops.get(input, "result").result().isEmpty()) {
                            newInput = ops.mergeToMap(input, ops.createString("result"), ops.createMap(
                                    Stream.of(Pair.of(
                                            ops.createString("item"),
                                            ops.createString(ItemRegistry.UMA_RACE_TICKET.getId().toString())
                                    ))
                            )).result().orElse(input);
                        }
                        else {
                            newInput = input;
                        }
                        var resultField = ops.get(newInput, "result").result();

                        var baseResult = compose().codec().decode(ops, newInput);
                        return baseResult.flatMap(basePair -> {
                            var extraResult = ResourceLocation.CODEC.optionalFieldOf("race").codec().decode(ops, newInput);
                            return extraResult.map(extraPair -> {
                                var output = extraPair.getFirst()
                                        .orElseGet(() -> // result.item MUST be present for the base recipe to even decode correctly
                                                ResourceLocation.CODEC.fieldOf("item").codec()
                                                        .decode(ops, resultField.orElseThrow())
                                                        .result()
                                                        .orElseThrow()
                                                        .getFirst()
                                        );

                                return Pair.of(converter.apply(basePair.getFirst(), output), newInput);
                            });
                        });
                    }
                }
        );
    }

    @Override
    public @Nullable U fromNetwork(FriendlyByteBuf pBuffer) {
        T recipe = compose().fromNetwork(pBuffer);
        if (pBuffer.readBoolean())
            return converter().apply(recipe, pBuffer.readResourceLocation());
        return converter().apply(recipe, null);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, U pRecipe) {
        compose().toNetwork(pBuffer, pRecipe);
        if (pRecipe instanceof ShapelessRaceTicketRecipe shapeless) {
            boolean hasName = shapeless.getKey() != null;
            pBuffer.writeBoolean(hasName);
            if (hasName) pBuffer.writeResourceLocation(shapeless.getKey());
        } else {
            pBuffer.writeBoolean(false);
        }
    }
}
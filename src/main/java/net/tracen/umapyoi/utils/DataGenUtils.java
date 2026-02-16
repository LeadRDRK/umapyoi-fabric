package net.tracen.umapyoi.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;

import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.tracen.umapyoi.client.model.pojo.CubesItem;

import java.lang.reflect.Type;

public class DataGenUtils {
    /* Copied from 1.21.1 */
    public static class IdentifierSerializer implements JsonDeserializer<Identifier>, JsonSerializer<Identifier> {
        public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Identifier.parse(GsonHelper.convertToString(json, "location"));
        }

        public JsonElement serialize(Identifier src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    public static final Gson DATA_GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting()
            .registerTypeAdapter(Identifier.class, new IdentifierSerializer())
            .registerTypeAdapter(CubesItem.class, new CubesItem.Deserializer()).create();

    public static final Codec<JsonElement> JSON_ELEMENT_CODEC = Codec.of(
            new Encoder<>() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> DataResult<T> encode(JsonElement input, DynamicOps<T> ops, T prefix) {
                    if (ops instanceof JsonOps) {
                        return DataResult.success((T) input);
                    }
                    return DataResult.error(() -> "Codec can only be used with JsonOps");
                }
            },
            new Decoder<>() {
                @Override
                public <T> DataResult<Pair<JsonElement, T>> decode(DynamicOps<T> ops, T input) {
                    if (ops instanceof JsonOps) {
                        return DataResult.success(Pair.of((JsonElement) input, input));
                    }
                    return DataResult.error(() -> "Codec can only be used with JsonOps");
                }
            }
    );
}
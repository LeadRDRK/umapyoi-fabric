package net.tracen.umapyoi.recipe;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.ItemRegistry;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class UmasoulIngredient implements CustomIngredient {
    private final Set<Holder<Item>> items;
    private final RequestUma request;

    public UmasoulIngredient(Set<Holder<Item>> items, RequestUma request) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a UmasoulIngredient with no items");
        }
        this.items = Collections.unmodifiableSet(items);
        this.request = request;
    }

    @Override
    public boolean test(ItemStack input) {
        if (input == null)
            return false;
        return items.contains(input.getItem()) && this.request.test(input);
    }

    @Override
    public Stream<Holder<Item>> getMatchingItems() {
        return items.stream();
    }

    @Override
    public boolean requiresTesting() {
        return true;
    }

    @Override
    public CustomIngredientSerializer<UmasoulIngredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements CustomIngredientSerializer<UmasoulIngredient> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public ResourceLocation getIdentifier() {
            return ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "umasoul");
        }

        @Override
        public MapCodec<UmasoulIngredient> getCodec() {
            Codec<Set<Holder<Item>>> itemSetCodec = ResourceLocation.CODEC
                    .xmap(
                            loc -> (Holder<Item>) BuiltInRegistries.ITEM.get(loc).orElseThrow(),
                            holder -> BuiltInRegistries.ITEM.getKey(holder.value())
                    )
                    .listOf()
                    .comapFlatMap(
                            list -> DataResult.success(Set.copyOf(list)),
                            set -> set.stream().sorted(Comparator.comparing(holder ->
                                    BuiltInRegistries.ITEM.getKey(holder.value()))).toList()
                    );

            // Codec for a single item
            Codec<Set<Holder<Item>>> singleItemCodec = ResourceLocation.CODEC
                    .xmap(
                            loc -> (Holder<Item>) BuiltInRegistries.ITEM.get(loc).orElseThrow(),
                            holder -> BuiltInRegistries.ITEM.getKey(holder.value())
                    )
                    .xmap(
                            Set::of,
                            set -> set.iterator().next()
                    );

            // Either single item or array of items
            Codec<Set<Holder<Item>>> itemsCodec = Codec.either(
                    singleItemCodec.fieldOf("item").codec(),
                    itemSetCodec.fieldOf("items").codec()
            ).xmap(
                    either -> either.map(Function.identity(), Function.identity()),
                    items -> items.size() == 1
                            ? Either.left(items)
                            : Either.right(items)
            );

            // Final codec with optional default
            return RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            itemsCodec.optionalFieldOf("items", Set.of(ItemRegistry.BLANK_UMA_SOUL.builtInRegistryHolder()))
                                    .forGetter(ingredient -> ingredient.items),
                            RequestUma.CODEC.fieldOf("request")
                                    .forGetter(ingredient -> ingredient.request)
                    ).apply(instance, UmasoulIngredient::new)
            );
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, UmasoulIngredient> getPacketCodec() {
            return StreamCodec.composite(
                    ByteBufCodecs.collection(
                            HashSet::new,
                            ByteBufCodecs.idMapper(id -> BuiltInRegistries.ITEM.get(id).orElseThrow(),
                                    holder -> BuiltInRegistries.ITEM.getId(holder.value()))
                    ), i -> i.items,
                    RequestUma.STREAM_CODEC, i -> i.request,
                    UmasoulIngredient::new
            );
        }
    }
}

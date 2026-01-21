package net.tracen.umapyoi.recipe;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.ItemRegistry;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class UmasoulIngredient implements CustomIngredient {
    private final Set<Item> items;
    private final RequestUma request;
    private final List<ItemStack> stacks;

    public UmasoulIngredient(Set<Item> items, RequestUma request) {
        this.stacks = items.stream().map(item -> {
            ItemStack stack = new ItemStack(item);
            // copy NBT to prevent the stack from modifying the original, as capabilities or
            // vanilla item durability will modify the tag
            request.initItemStack(stack);
            return stack;
        }).toList();
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a UmasoulIngredient with no items");
        }
        this.items = Collections.unmodifiableSet(items);
        this.request = request;
    }

    public static UmasoulIngredient of(ItemLike item, RequestUma request) {
        return new UmasoulIngredient(Set.of(item.asItem()), request);
    }

    public static UmasoulIngredient of(RequestUma request) {
        return new UmasoulIngredient(Set.of(ItemRegistry.BLANK_UMA_SOUL.get()), request);
    }

    @Override
    public boolean test(ItemStack input) {
        if (input == null)
            return false;
        return items.contains(input.getItem()) && this.request.test(input);
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        return stacks;
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
            return new ResourceLocation(Umapyoi.MODID, "umasoul");
        }

        @Override
        public MapCodec<UmasoulIngredient> getCodec(boolean allowEmpty) {
            Codec<Set<Item>> itemSetCodec = ResourceLocation.CODEC
                    .xmap(
                            BuiltInRegistries.ITEM::get,
                            BuiltInRegistries.ITEM::getKey
                    )
                    .listOf()
                    .comapFlatMap(
                            list -> DataResult.success(ImmutableSet.copyOf(list)),
                            set -> set.stream().sorted(Comparator.comparing(BuiltInRegistries.ITEM::getKey)).toList()
                    );

            // Codec for a single item
            Codec<Set<Item>> singleItemCodec = ResourceLocation.CODEC
                    .xmap(
                            BuiltInRegistries.ITEM::get,
                            BuiltInRegistries.ITEM::getKey
                    )
                    .xmap(
                            Set::of,
                            set -> set.iterator().next()
                    );

            // Either single item or array of items
            Codec<Set<Item>> itemsCodec = Codec.either(
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
                            itemsCodec.optionalFieldOf("items", Set.of(ItemRegistry.BLANK_UMA_SOUL.get()))
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
                            ByteBufCodecs.idMapper(Item::byId, Item::getId)
                    ), i -> i.items,
                    RequestUma.STREAM_CODEC, i -> i.request,
                    UmasoulIngredient::new
            );
        }
    }
}

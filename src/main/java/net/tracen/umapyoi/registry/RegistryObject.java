package net.tracen.umapyoi.registry;

import com.mojang.datafixers.util.Either;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class RegistryObject<T> implements Supplier<T>, Holder<T> {
    private final ResourceLocation name;
    private T value = null;

    public RegistryObject(ResourceLocation name) {
        this.name = name;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public ResourceLocation getId() {
        return name;
    }

    @Override
    @Nullable
    public T get() {
        return value;
    }

    @Override
    @MethodsReturnNonnullByDefault
    public T value() {
        return value;
    }

    @Override
    public boolean isBound() {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean is(ResourceLocation location) {
        return name == location;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean is(ResourceKey<T> resourceKey) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean is(Predicate<ResourceKey<T>> predicate) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean is(TagKey<T> tagKey) {
        return false;
    }

    @Override
    public boolean is(Holder<T> holder) {
        return this.value.equals(holder.value());
    }

    @Override
    @MethodsReturnNonnullByDefault
    public Stream<TagKey<T>> tags() {
        return Stream.empty();
    }

    @Override
    @MethodsReturnNonnullByDefault
    public Either<ResourceKey<T>, T> unwrap() {
        return Either.right(this.value);
    }

    @Override
    @MethodsReturnNonnullByDefault
    public Optional<ResourceKey<T>> unwrapKey() {
        return Optional.empty();
    }

    @Override
    @MethodsReturnNonnullByDefault
    public Kind kind() {
        return Holder.Kind.DIRECT;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canSerializeIn(HolderOwner<T> owner) {
        return true;
    }
}

package net.tracen.umapyoi.registry;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.function.Supplier;

public class RegistryObject<T> implements Supplier<T> {
    private final ResourceLocation name;
    private Holder<T> holder = null;

    public RegistryObject(ResourceLocation name) {
        this.name = name;
    }

    public void bindHolder(Holder<T> holder) {
        if (this.holder != null) {
            throw new IllegalStateException("Cannot change the holder of a RegistryObject");
        }
        this.holder = holder;
    }

    public ResourceLocation getId() {
        return name;
    }

    @Override
    public T get() {
        return getHolder().value();
    }

    public Holder<T> getHolder() {
        return Objects.requireNonNull(holder);
    }
}

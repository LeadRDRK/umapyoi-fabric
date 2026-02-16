package net.tracen.umapyoi.registry;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;

import java.util.Objects;
import java.util.function.Supplier;

public class RegistryObject<T> implements Supplier<T> {
    private final Identifier name;
    private Holder<T> holder = null;

    public RegistryObject(Identifier name) {
        this.name = name;
    }

    public void bindHolder(Holder<T> holder) {
        if (this.holder != null) {
            throw new IllegalStateException("Cannot change the holder of a RegistryObject");
        }
        this.holder = holder;
    }

    public Identifier getId() {
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

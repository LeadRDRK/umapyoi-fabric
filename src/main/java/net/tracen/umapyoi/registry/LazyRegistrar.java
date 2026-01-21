package net.tracen.umapyoi.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class LazyRegistrar<T> {
    private final String namespace;
    private final Map<RegistryObject<T>, Supplier<? extends T>> entries = new LinkedHashMap<>();
    private final Set<RegistryObject<T>> entriesView = Collections.unmodifiableSet(entries.keySet());
    private final Supplier<Registry<T>> registrySupplier;

    private LazyRegistrar(ResourceKey<? extends Registry<T>> key, String namespace) {
        this.namespace = namespace;
        this.registrySupplier = new RegistrySupplier(key);
    }

    public static <B> LazyRegistrar<B> create(ResourceKey<? extends Registry<B>> key, String namespace) {
        return new LazyRegistrar<>(key, namespace);
    }

    public <B extends T> RegistryObject<B> register(String path, final Supplier<? extends B> entry) {
        return register(ResourceLocation.fromNamespaceAndPath(namespace, path), entry);
    }

    @SuppressWarnings("unchecked")
    public <B extends T> RegistryObject<B> register(ResourceLocation name, final Supplier<? extends B> entry) {
        RegistryObject<B> obj = new RegistryObject<>(name);
        if (entries.putIfAbsent((RegistryObject<T>) obj, entry) != null) {
            throw new IllegalArgumentException("Entry already exists: " + name);
        }
        return obj;
    }

    public void register() {
        Registry<T> registry = makeRegistry().get();
        entries.forEach((obj, supplier) -> {
            T value = supplier.get();
            Registry.register(registry, obj.getId(), value);
            obj.setValue(value);
            if (value instanceof RegistryNameHolder holder) {
                holder.setRegistryName(obj.getId());
            }
        });
    }

    public Supplier<Registry<T>> makeRegistry() {
        return registrySupplier;
    }

    public Collection<RegistryObject<T>> getEntries() {
        return entriesView;
    }

    private class RegistrySupplier implements Supplier<Registry<T>> {
        private final ResourceKey<? extends Registry<T>> key;
        private Registry<T> registry = null;

        private RegistrySupplier(ResourceKey<? extends Registry<T>> key) {
            this.key = key;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Registry<T> get() {
            if (this.registry == null) {
                // Check if reg is built in, else create registry
                if (BuiltInRegistries.REGISTRY.containsKey(key.location()))
                    this.registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(key.location());
                else
                    this.registry = FabricRegistryBuilder
                            .createSimple((ResourceKey<Registry<T>>) key)
                            .buildAndRegister();
            }
            return this.registry;
        }
    }
}

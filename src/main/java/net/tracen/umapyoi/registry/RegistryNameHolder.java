package net.tracen.umapyoi.registry;

import net.minecraft.resources.Identifier;

import javax.annotation.Nullable;

public class RegistryNameHolder {
    @Nullable
    private Identifier name;

    protected RegistryNameHolder() {
        this.name = null;
    }

    public void setRegistryName(Identifier name) {
        this.name = name;
    }

    public final Identifier getRegistryName() {
        return name;
    }
}

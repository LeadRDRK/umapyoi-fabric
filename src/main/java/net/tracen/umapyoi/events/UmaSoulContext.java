package net.tracen.umapyoi.events;

import net.minecraft.world.item.ItemStack;

public abstract class UmaSoulContext {
    protected ItemStack soul;
    public UmaSoulContext(ItemStack soul) {
        this.soul = soul;
    }

    public ItemStack getUmaSoul() {
        return soul;
    }
    public void setUmaSoul(ItemStack soul) {
        this.soul = soul;
    }

}
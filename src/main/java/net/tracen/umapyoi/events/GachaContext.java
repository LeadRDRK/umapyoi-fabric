package net.tracen.umapyoi.events;

import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;

public abstract class GachaContext {
    private final Collection<Identifier> fulfills;
    private final Identifier target;
    private final ItemStack defaultResult;
    private ItemStack output = null;
    private final RandomSource rnd;
    private final ItemStack input;

    public GachaContext(ItemStack input, Collection<Identifier> fulfills, Identifier target, ItemStack defaultResult, RandomSource src) {
        this.fulfills = fulfills;
        this.target = target;
        this.defaultResult = defaultResult;
        this.rnd = src;
        this.input = input;
    }

    public ItemStack getInput() {
        return this.input;
    }

    public RandomSource getRandomSource() {
        return this.rnd;
    }

    public ItemStack getOutput() {
        return Objects.requireNonNullElse(this.output, this.defaultResult);
    }

    public void setOutput(@Nullable ItemStack output) {
        this.output = output;
    }

    public Identifier getOriginalTarget() {
        return this.target;
    }

    public Collection<Identifier> getOriginalFulfills() {
        return this.fulfills;
    }

    public abstract String getType();
}
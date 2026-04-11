package net.tracen.umapyoi.registry.factors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.events.ApplyFactorCallback;
import net.tracen.umapyoi.registry.UmaFactorRegistry;

import java.util.Comparator;
import java.util.Optional;

import javax.annotation.Nullable;

public class UmaFactorStack {
    private final UmaFactor factor;
    private final int level;
    @Nullable
    private CompoundTag tag;

    public static final Codec<UmaFactorStack> CODEC = RecordCodecBuilder
            .create(instance -> instance
                    .group(UmaFactor.CODEC.fieldOf("factor").forGetter(UmaFactorStack::getFactor),
                            Codec.INT.fieldOf("level").forGetter(UmaFactorStack::getLevel),
                            CompoundTag.CODEC.optionalFieldOf("Tag")
                                    .forGetter(stack -> Optional.ofNullable(stack.getTag())))
                    .apply(instance, UmaFactorStack::new));

    public static class UmaFactorStackComparator implements Comparator<UmaFactorStack> {
        public static UmaFactorStackComparator INSTANCE = new UmaFactorStackComparator();

        @Override
        public int compare(UmaFactorStack o1, UmaFactorStack o2) {
            UmaFactor leftFactor = o1.getFactor();
            UmaFactor rightFactor = o2.getFactor();
            if (leftFactor != rightFactor) {
                Identifier leftLoc = UmaFactorRegistry.REGISTRY.get().getKey(leftFactor);
                Identifier rightLoc = UmaFactorRegistry.REGISTRY.get().getKey(rightFactor);
                return UmaFactor.UmaFactorComparator.compare(leftFactor, leftLoc, rightFactor, rightLoc);
            }
            return o1.level - o2.level;
        }
    }

    public UmaFactorStack(UmaFactor factor, int level) {
        this.factor = factor;
        this.level = level;
    }

    public UmaFactorStack(UmaFactor factor, int level, CompoundTag nbt) {
        this(factor, level);
        if (nbt != null) {
            tag = nbt.copy();
        }
    }

    public UmaFactorStack(UmaFactor factor, int level, Optional<CompoundTag> nbt) {
        this(factor, level);
        nbt.ifPresent(this::setTag);
    }

    public UmaFactor getFactor() {
        return factor;
    }

    public int getLevel() {
        return level;
    }

    public void applyFactor(ItemStack soul) {
        var event = new ApplyFactorCallback.Context(this, soul);
        if (!ApplyFactorCallback.Pre.invoke(event)) {
            this.getFactor().applyFactor(soul, this);
            ApplyFactorCallback.Post.invoke(event);
        }
    }

    public Component getDescription() {
        return this.getFactor().getDescription(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof UmaFactorStack stack)
            return stack.level == this.level && stack.factor == this.factor;
        return false;
    }

    public boolean equalsFactorStackIgnoreVersion(UmaFactorStack other) {
        if (other == this) return true;
        if (other == null) return false;
        UmaFactor thisFactor = this.factor;
        return thisFactor.withStackEquals(this, other);
    }

    @Override
    public int hashCode() {
        int code = 31 * Integer.hashCode(getLevel()) + this.getFactor().getRegistryName().hashCode();
        if (tag != null)
            code = 31 * code + tag.hashCode();
        return code;
    }

    @Override
    public String toString() {
        return String.format("level:%d, factor:%s", this.getLevel(), this.factor);
    }

    // Copied from ItemStack, for Tags.

    @Nullable
    public CompoundTag getTag() {
        return this.tag;
    }

    public CompoundTag getOrCreateTag() {
        if (this.tag == null) {
            this.setTag(new CompoundTag());
        }
        return this.tag;
    }

    /**
     * Assigns a NBTTagCompound to the stack
     */
    public void setTag(@Nullable CompoundTag tag) {
        this.tag = tag;
    }

    public Component getDescriptionDetail() {
        return this.getFactor().getDescriptionDetail(this);
    }
}

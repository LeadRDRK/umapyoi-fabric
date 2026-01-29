package net.tracen.umapyoi.registry.training;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.events.ApplyTrainingSupportCallback;

import java.util.Optional;

import javax.annotation.Nullable;

public class SupportStack {
    private final TrainingSupport factor;
    private final int level;
    @Nullable
    private CompoundTag tag;

    public static final Codec<SupportStack> CODEC = RecordCodecBuilder
            .create(instance -> instance
                    .group(TrainingSupport.CODEC.fieldOf("support").forGetter(SupportStack::getFactor),
                            Codec.INT.fieldOf("level").forGetter(SupportStack::getLevel),
                            CompoundTag.CODEC.optionalFieldOf("tag")
                                    .forGetter(stack -> Optional.ofNullable(stack.getTag())))
                    .apply(instance, SupportStack::new));

    public static final SupportStack EMPTY = new SupportStack(null, 0);

    public SupportStack(TrainingSupport factor, int level) {
        this.factor = factor;
        this.level = level;
    }

    public SupportStack(TrainingSupport factor, int level, CompoundTag nbt) {
        this(factor, level);
        if (nbt != null) {
            tag = nbt.copy();
        }
    }

    public SupportStack(TrainingSupport factor, int level, Optional<CompoundTag> nbt) {
        this(factor, level);
        nbt.ifPresent(this::setTag);
    }

    public TrainingSupport getFactor() {
        return factor;
    }

    public int getLevel() {
        return level;
    }

    public boolean isEmpty() {
        if (this == EMPTY) {
            return true;
        } else if (this.getFactor() != null) {
            return this.getLevel() <= 0;
        } else {
            return true;
        }
    }

    public boolean applySupport(ItemStack soul, RandomSource rand) {
        var event = new ApplyTrainingSupportCallback.Context(this, soul);
        if (!ApplyTrainingSupportCallback.Pre.invoke(event)) {
            boolean result = this.getFactor().applySupport(soul, rand, this);
            return result && !ApplyTrainingSupportCallback.Post.invoke(event);
        } else
            return false;
    }

    public Component getDescription() {
        return this.getFactor().getDescription(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof SupportStack stack)
            return stack.level == this.level && stack.factor == this.factor;
        return false;
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
        return String.format("level:%d, support:%s", this.getLevel(), this.factor);
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
}

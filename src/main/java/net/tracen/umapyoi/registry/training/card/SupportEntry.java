package net.tracen.umapyoi.registry.training.card;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;

import java.util.Optional;

import javax.annotation.Nullable;

public class SupportEntry {
    private final Identifier support;
    private final int level;
    @Nullable
    private CompoundTag tag;

    public static final Codec<SupportEntry> CODEC = RecordCodecBuilder
            .create(instance -> instance
                    .group(Identifier.CODEC.fieldOf("support").forGetter(SupportEntry::getFactor),
                            Codec.INT.fieldOf("level").forGetter(SupportEntry::getLevel),
                            CompoundTag.CODEC.optionalFieldOf("tag")
                                    .forGetter(stack -> Optional.ofNullable(stack.getTag())))
                    .apply(instance, SupportEntry::new));

    public SupportEntry(Identifier support, int level) {
        this.support = support;
        this.level = level;
    }

    public SupportEntry(Identifier support, int level, CompoundTag nbt) {
        this(support, level);
        if (nbt != null) {
            tag = nbt.copy();
        }
    }

    public SupportEntry(Identifier support, int level, Optional<CompoundTag> nbt) {
        this(support, level);
        nbt.ifPresent(this::setTag);
    }

    public Identifier getFactor() {
        return support;
    }

    public int getLevel() {
        return level;
    }

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

    public void setTag(@Nullable CompoundTag tag) {
        this.tag = tag;
    }
}

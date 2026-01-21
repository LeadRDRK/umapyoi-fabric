package net.tracen.umapyoi.registry.umadata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import io.netty.buffer.ByteBuf;

public record UmaDataTraining(int physique, int talent) {
    public static final Codec<UmaDataTraining> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT.fieldOf("physique").forGetter(UmaDataTraining::physique),
                    Codec.INT.fieldOf("talent").forGetter(UmaDataTraining::talent))
            .apply(instance, UmaDataTraining::new));

    public static final StreamCodec<ByteBuf, UmaDataTraining> STREAM = StreamCodec.composite(
            ByteBufCodecs.INT, UmaDataTraining::physique,
            ByteBufCodecs.INT, UmaDataTraining::talent,
            UmaDataTraining::new
    );
}
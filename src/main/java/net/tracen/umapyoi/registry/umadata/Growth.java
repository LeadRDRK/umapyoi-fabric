package net.tracen.umapyoi.registry.umadata;

import com.mojang.serialization.Codec;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import io.netty.buffer.ByteBuf;

public enum Growth implements StringRepresentable {
    UNTRAINED, TRAINED, RETIRED;

    public static final Codec<Growth> CODEC = StringRepresentable.fromEnum(Growth::values);
    public static final StreamCodec<ByteBuf, Growth> STREAM = ByteBufCodecs.STRING_UTF8.map(
            Growth::valueOf,
            Growth::name
    );

    @Override
    @MethodsReturnNonnullByDefault
    public String getSerializedName() {
        return this.name();
    }
}

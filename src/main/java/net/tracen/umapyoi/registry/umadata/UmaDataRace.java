package net.tracen.umapyoi.registry.umadata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.tracen.umapyoi.utils.Aptitude;

import java.util.Arrays;
import java.util.List;

import io.netty.buffer.ByteBuf;

public record UmaDataRace(
        List<Aptitude> surfaceAptitude,
        List<Aptitude> distanceAptitude
) {
    public static final UmaDataRace DEFAULT = new UmaDataRace(
            Arrays.stream(UmaData.DEFAULT_SURFACE_APTITUDE).toList(),
            Arrays.stream(UmaData.DEFAULT_DISTANCE_APTITUDE).toList()
    );

    public UmaDataRace(Aptitude[] surfaceAptitude, Aptitude[] distanceAptitude) {
        this(Arrays.stream(surfaceAptitude).toList(), Arrays.stream(distanceAptitude).toList());
    }

    public static final Codec<UmaDataRace> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Aptitude.CODEC.listOf().fieldOf("surface_aptitude").forGetter(UmaDataRace::surfaceAptitude),
            Aptitude.CODEC.listOf().fieldOf("distance_aptitude").forGetter(UmaDataRace::distanceAptitude)
    ).apply(instance, UmaDataRace::new));

    public static final StreamCodec<ByteBuf, UmaDataRace> STREAM_CODEC = StreamCodec.composite(
            Aptitude.STREAM_CODEC.apply(ByteBufCodecs.list()), UmaDataRace::surfaceAptitude,
            Aptitude.STREAM_CODEC.apply(ByteBufCodecs.list()), UmaDataRace::distanceAptitude,
            UmaDataRace::new
    );
}

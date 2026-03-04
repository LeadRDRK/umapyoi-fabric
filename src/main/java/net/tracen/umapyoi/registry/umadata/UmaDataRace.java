package net.tracen.umapyoi.registry.umadata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.tracen.umapyoi.utils.Aptitude;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.netty.buffer.ByteBuf;

public record UmaDataRace(
        List<Aptitude> surfaceAptitude,
        List<Aptitude> distanceAptitude,
        Set<ResourceLocation> wonRaces,
        Map<ResourceLocation, Integer> attended,
        int lastAttendTime,
        boolean hasDebut
) {
    public static final UmaDataRace DEFAULT = new UmaDataRace(
            Arrays.stream(UmaData.DEFAULT_SURFACE_APTITUDE).toList(),
            Arrays.stream(UmaData.DEFAULT_DISTANCE_APTITUDE).toList(),
            Collections.emptySet(),
            Collections.emptyMap(),
            0,
            false
    );

    public UmaDataRace(Aptitude[] surfaceAptitude, Aptitude[] distanceAptitude) {
        this(
                Arrays.stream(surfaceAptitude).toList(),
                Arrays.stream(distanceAptitude).toList(),
                DEFAULT.wonRaces,
                DEFAULT.attended,
                DEFAULT.lastAttendTime,
                DEFAULT.hasDebut
        );
    }

    public UmaDataRace update(
            Set<ResourceLocation> wonRaces,
            Map<ResourceLocation, Integer> attended,
            int lastAttendTime,
            boolean hasDebut
    ) {
        return new UmaDataRace(
                this.surfaceAptitude,
                this.distanceAptitude,
                wonRaces,
                attended,
                lastAttendTime,
                hasDebut
        );
    }

    public static final Codec<UmaDataRace> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Aptitude.CODEC.listOf().fieldOf("surface_aptitude").forGetter(UmaDataRace::surfaceAptitude),
            Aptitude.CODEC.listOf().fieldOf("distance_aptitude").forGetter(UmaDataRace::distanceAptitude),
            ResourceLocation.CODEC.listOf().xmap(Set::copyOf, List::copyOf).fieldOf("won_races").forGetter(UmaDataRace::wonRaces),
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("attended").forGetter(UmaDataRace::attended),
            Codec.INT.fieldOf("last_attend_time").forGetter(UmaDataRace::lastAttendTime),
            Codec.BOOL.fieldOf("has_debut").forGetter(UmaDataRace::hasDebut)
    ).apply(instance, UmaDataRace::new));

    public static final StreamCodec<ByteBuf, UmaDataRace> STREAM_CODEC = StreamCodec.composite(
            Aptitude.STREAM_CODEC.apply(ByteBufCodecs.list()), UmaDataRace::surfaceAptitude,
            Aptitude.STREAM_CODEC.apply(ByteBufCodecs.list()), UmaDataRace::distanceAptitude,
            ByteBufCodecs.collection(HashSet::new, ResourceLocation.STREAM_CODEC), UmaDataRace::wonRaces,
            ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.INT), UmaDataRace::attended,
            ByteBufCodecs.INT, UmaDataRace::lastAttendTime,
            ByteBufCodecs.BOOL, UmaDataRace::hasDebut,
            UmaDataRace::new
    );
}

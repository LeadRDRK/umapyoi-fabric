package net.tracen.umapyoi.registry.umadata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.netty.buffer.ByteBuf;

public record UmaDataRaceStatus(
        Set<ResourceLocation> wonRaces,
        Map<ResourceLocation, Integer> attended,
        int lastAttendTime,
        boolean hasDebut,
        Map<ResourceLocation, Set<ResourceLocation>> attendRaceTag,
        Map<ResourceLocation, Integer> attendRaceTagUnique
) {
    public static final UmaDataRaceStatus DEFAULT = new UmaDataRaceStatus(
            Collections.emptySet(),
            Collections.emptyMap(),
            0,
            false,
            Collections.emptyMap(),
            Collections.emptyMap()
    );

    public static final Codec<UmaDataRaceStatus> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.listOf().xmap(Set::copyOf, List::copyOf).fieldOf("won_races").forGetter(UmaDataRaceStatus::wonRaces),
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("attended").forGetter(UmaDataRaceStatus::attended),
            Codec.INT.fieldOf("last_attend_time").forGetter(UmaDataRaceStatus::lastAttendTime),
            Codec.BOOL.fieldOf("has_debut").forGetter(UmaDataRaceStatus::hasDebut),
            Codec.unboundedMap(ResourceLocation.CODEC, ResourceLocation.CODEC.listOf().xmap(Set::copyOf, List::copyOf)).fieldOf("attend_race_tag").forGetter(UmaDataRaceStatus::attendRaceTag),
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("attend_race_tag_unique").forGetter(UmaDataRaceStatus::attendRaceTagUnique)
    ).apply(instance, UmaDataRaceStatus::new));

    public static final StreamCodec<ByteBuf, UmaDataRaceStatus> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(HashSet::new, ResourceLocation.STREAM_CODEC), UmaDataRaceStatus::wonRaces,
            ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.INT), UmaDataRaceStatus::attended,
            ByteBufCodecs.INT, UmaDataRaceStatus::lastAttendTime,
            ByteBufCodecs.BOOL, UmaDataRaceStatus::hasDebut,
            ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.collection(HashSet::new, ResourceLocation.STREAM_CODEC)), UmaDataRaceStatus::attendRaceTag,
            ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.INT), UmaDataRaceStatus::attendRaceTagUnique,
            UmaDataRaceStatus::new
    );
}

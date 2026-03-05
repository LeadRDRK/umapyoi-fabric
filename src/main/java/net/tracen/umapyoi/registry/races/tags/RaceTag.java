package net.tracen.umapyoi.registry.races.tags;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.races.Race;
import net.tracen.umapyoi.registry.umadata.UmaDataBasicStatus;
import net.tracen.umapyoi.registry.umadata.UmaDataRaceStatus;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

public record RaceTag(int maximum, ResourceLocation id, boolean isUnique, int[] propertyReward) {
    public static final Codec<RaceTag> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Codec.INT.fieldOf("max").forGetter(RaceTag::maximum),
                    ResourceLocation.CODEC.fieldOf("id").forGetter(RaceTag::id),
                    Codec.BOOL.fieldOf("is_unique").forGetter(RaceTag::isUnique),
                    Codec.INT_STREAM.xmap(IntStream::toArray, Arrays::stream)
                            .optionalFieldOf("property_reward", new int[5])
                            .forGetter(RaceTag::propertyReward)
            ).apply(instance, RaceTag::new));

    public static final ResourceKey<Registry<RaceTag>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(new ResourceLocation(Umapyoi.MODID, "race_tags"));

    public boolean applyToUmaSoul(ItemStack soul, Race race) {
        boolean isFulfill;
        var raceData = UmaSoulUtils.getRaceStatus(soul);
        var attendRaceTagUnique = raceData.attendRaceTagUnique();
        var attendRaceTag = raceData.attendRaceTag();
        if (!this.isUnique) {
            attendRaceTagUnique = new HashMap<>(attendRaceTagUnique);
            int current = attendRaceTagUnique.getOrDefault(this.id, 0);
            if (current >= this.maximum) return false;
            attendRaceTagUnique.put(this.id, ++current);
            isFulfill = current == this.maximum;
        } else {
            attendRaceTag = new HashMap<>(attendRaceTag);
            var races = new HashSet<>(attendRaceTag.get(this.id));
            int current = races.size();
            if (current >= this.maximum || !races.add(race.id)) return false;
            attendRaceTag.put(this.id, races);
            isFulfill = races.size() == this.maximum;
        }
        if (isFulfill) {
            int[] properties = UmaSoulUtils.getProperty(soul).array();
            int[] propertiesCeil = UmaSoulUtils.getMaxProperty(soul).array();

            for (int i = 0; i < 5; i++) {
                propertiesCeil[i] = Math.min(propertiesCeil[i] + propertyReward[i], 39);
            }

            for (int i = 0; i < 5; i++) {
                properties[i] = Math.min(properties[i] + propertyReward[i], propertiesCeil[i]);
            }

            soul.set(DataComponentsTypeRegistry.UMADATA_BASIC_STATUS.get(), UmaDataBasicStatus.init(properties));
            soul.set(DataComponentsTypeRegistry.UMADATA_MAX_BASIC_STATUS.get(), UmaDataBasicStatus.init(propertiesCeil));
        }
        soul.set(DataComponentsTypeRegistry.UMADATA_RACE_STATUS.get(), new UmaDataRaceStatus(
                raceData.wonRaces(), raceData.attended(), raceData.lastAttendTime(), raceData.hasDebut(),
                attendRaceTag, attendRaceTagUnique
        ));
        return true;
    }

    public static Map<ResourceLocation, Integer> queryUmaSoulTags(ItemStack soul) {
        var raceData = UmaSoulUtils.getRaceStatus(soul);
        var attendRaceTag = raceData.attendRaceTag();
        var attendRaceTagUnique = raceData.attendRaceTagUnique();
        if (attendRaceTag.isEmpty() && attendRaceTagUnique.isEmpty())
            return Map.of();

        HashMap<ResourceLocation, Integer> hmap = new HashMap<>();
        attendRaceTag.keySet().forEach(l -> hmap.put(l, queryUmaSoulTagCount(raceData, l)));
        attendRaceTagUnique.keySet().forEach(l -> hmap.put(l, queryUmaSoulTagCount(raceData, l)));
        return hmap;
    }

    public static int queryUmaSoulTagCount(ItemStack soul, ResourceLocation id) {
        var raceData = UmaSoulUtils.getRaceStatus(soul);
        return queryUmaSoulTagCount(raceData, id);
    }

    public static int queryUmaSoulTagCount(UmaDataRaceStatus raceData, ResourceLocation id) {
        return Optional.ofNullable(raceData.attendRaceTagUnique().get(id))
                .or(() -> Optional.ofNullable(raceData.attendRaceTag().get(id)).map(Set::size))
                .orElse(0);
    }

    public int queryUmaSoulTagCount(ItemStack soul) {
        return queryUmaSoulTagCount(soul, this.id);
    }
}

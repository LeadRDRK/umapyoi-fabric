package net.tracen.umapyoi.registry.races;

import static net.tracen.umapyoi.registry.races.field.RaceFieldRegistry.CONST_ADAPTIVE;

import com.google.common.base.Functions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.races.field.RaceField;
import net.tracen.umapyoi.registry.races.tags.RaceTag;
import net.tracen.umapyoi.registry.umadata.Growth;
import net.tracen.umapyoi.registry.umadata.Motivations;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.registry.umadata.UmaDataRaceStatus;
import net.tracen.umapyoi.utils.Distance;
import net.tracen.umapyoi.utils.Position;
import net.tracen.umapyoi.utils.RaceRanking;
import net.tracen.umapyoi.utils.Surface;
import net.tracen.umapyoi.utils.UmaSoulUtils;
import net.tracen.umapyoi.utils.Year;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Race {
    public static final ResourceKey<Registry<Race>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(Identifier.fromNamespaceAndPath(Umapyoi.MODID, "races"));

    public static Codec<Race> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Identifier.CODEC.fieldOf("id").forGetter(Race::id),
                    RaceRanking.CODEC.fieldOf("ranking").forGetter(Race::ranking),
                    Codec.INT.fieldOf("length").forGetter(Race::length),
                    Codec.INT.fieldOf("time").forGetter(Race::time),
                    Year.CODEC.listOf().fieldOf("year").forGetter((r) -> r.year.stream().toList()),
                    Surface.CODEC.fieldOf("surface").forGetter(Race::surface),
                    Identifier.CODEC.listOf().fieldOf("tags").forGetter((r) -> r.tags.stream().toList()),
                    Identifier.CODEC.fieldOf("field").forGetter(Race::field),
                    Codec.INT.listOf().optionalFieldOf("attribute_correction", List.of()).forGetter((r) -> r.attrCorr.stream().toList()),
                    Codec.INT.optionalFieldOf("reference_level", 5).forGetter(Race::referenceLevel),
                    Codec.STRING.xmap(s -> Growth.valueOf(s.toUpperCase()), g -> g.name().toLowerCase()).listOf().optionalFieldOf("allow_status", List.of(Growth.TRAINED, Growth.RETIRED)).forGetter((r) -> r.allowStatus.stream().toList()),
                    Codec.BOOL.optionalFieldOf("exclusive", true).forGetter(Race::exclusive),
                    Codec.INT.optionalFieldOf("later_then", 0).forGetter(Race::laterThen),
                    Identifier.CODEC.listOf().optionalFieldOf("after_race", List.of()).forGetter((r) -> r.afterRace.stream().toList()),
                    Codec.STRING.optionalFieldOf("texture_predicate_override").forGetter(r -> Optional.ofNullable(r.texturePredicateOverride))
            ).apply(instance, Race::new)
    );

    public final Identifier id;
    public Identifier id() { return this.id; }

    public final RaceRanking ranking;
    public RaceRanking ranking() { return this.ranking; }

    public final int length;
    public int length() {
        return this.length;
    }

    public Distance distance(ItemStack stack) {
        return this.distance != Distance.ADAPTIVE ? this.distance : Distance.AdaptiveEvaluation(stack);
    }

    public int length(ItemStack stack) {
        return this.distance != Distance.ADAPTIVE ? this.length : Distance.AdaptiveEvaluation(stack).defaultLength;
    }

    public final int time;
    public int time() {
        return this.time;
    }

    public final Set<Year> year;
    public final Set<Identifier> tags;
    public final Surface surface;
    public Surface surface() { return this.surface; }
    public Surface surface(ItemStack stack) {
        return this.surface != Surface.ADAPTIVE ? this.surface : Surface.AdaptiveCollapse(stack);
    }
    public final Distance distance;
    public final Identifier field;
    public Identifier field() { return this.field; }
    public RaceField field(Level world, ItemStack stack) {
        Registry<RaceField> registry = UmapyoiAPI.getRaceFieldRegistry(world);
        if (this.field.equals(CONST_ADAPTIVE)) {
            Map<RaceField, Integer> makeupMap = registry.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toMap(
                    Functions.identity(),
                    field -> field.compareToAbs(this.distance(stack), this.surface(stack))
            ));
            int minimum = makeupMap.values().stream().filter(i -> i != -1).min(Comparator.naturalOrder()).orElse(-1);
            List<RaceField> fieldCandidate = makeupMap.entrySet().stream().filter(et -> (minimum == -1 || et.getValue() == minimum)).map(Map.Entry::getKey).sorted().toList(); // 确保可复现性
            return fieldCandidate.get(world.getRandom().nextInt(0, fieldCandidate.size()));
        }
        return registry.get(this.field)
                .map(Holder::value)
                .orElse(null);
    }
    public final Set<Integer> attrCorr;
    private final double[] correction;
    public final int referenceLevel;
    public int referenceLevel() { return this.referenceLevel; }
    public final Set<Growth> allowStatus;
    public final boolean exclusive;
    public boolean exclusive() { return this.exclusive; }
    public final int laterThen;
    public int laterThen() { return this.laterThen; }
    public final Set<Identifier> afterRace;
    public final String texturePredicateOverride;
    public String texturePredicateOverride() {
        return this.texturePredicateOverride;
    }
    public Race(Identifier id, RaceRanking ranking, int length, int time, Set<Year> year, Surface surface,
                Set<Identifier> tags, Identifier field, Set<Integer> attrCorr, int referenceLevel, Set<Growth> allowStatus,
                boolean exclusive, int laterThen, Set<Identifier> afterRace, String texturePredicateOverride) {
        this.id = id;
        this.ranking = ranking;
        this.length = length;
        this.time = time;
        this.year = year;
        this.surface = surface;
        this.distance = Distance.match(length);
        this.tags = tags;
        this.field = field;
        this.attrCorr = attrCorr;
        this.correction = new double[]{
                attrCorr.contains(0) ? 1.05 : 1,
                attrCorr.contains(1) ? 1.05 : 1,
                attrCorr.contains(2) ? 1.05 : 1,
                attrCorr.contains(3) ? 1.05 : 1,
                attrCorr.contains(4) ? 1.05 : 1
        };
        this.referenceLevel = referenceLevel;
        this.allowStatus = allowStatus;
        this.exclusive = exclusive;
        this.laterThen = laterThen;
        this.afterRace = afterRace;
        this.texturePredicateOverride = texturePredicateOverride;
    }

    public Race(Identifier id, RaceRanking ranking, int length, int time, List<Year> year, Surface surface,
                List<Identifier> tags, Identifier field, List<Integer> attrCorr, int referenceLevel, List<Growth> allowStatus,
                boolean exclusive, int laterThen, List<Identifier> afterRace, Optional<String> texture) {
        this(id, ranking, length, time,
                new HashSet<>(year),
                surface,
                new HashSet<>(tags),
                field,
                new HashSet<>(attrCorr),
                referenceLevel,
                new HashSet<>(allowStatus),
                exclusive, laterThen, new HashSet<>(afterRace), texture.orElse(null)
        );
    }

    public boolean isAvailableToUmaSoul(ItemStack stack) {
        if (this.id.equals(RaceRegistry.DEFAULT.identifier())) return false;
        if (!(stack.is(ItemRegistry.UMA_SOUL) &&
                ((this.ranking == RaceRanking.DEBUT) ^ UmaSoulUtils.hasUmaSoulDebut(stack)))) return false;
        var raceData = UmaSoulUtils.getRaceStatus(stack);
        int last = raceData.lastAttendTime();
        if (this.exclusive) {
            boolean canAttend = false;
            for (Year year: this.year) {
                if (last < year.ordinal() * 24 + this.time) {
                    canAttend = true;
                    break;
                }
            }
            if (!canAttend) return false;
        }
        if (!this.afterRace.isEmpty()) {
            var attended = raceData.attended();
            boolean canAttend = false;
            for (Identifier key : attended.keySet()){
                if (this.afterRace.contains(key)) {
                    canAttend = true;
                    break;
                }
            }
            if (!canAttend) return false;
        }
        return this.allowStatus.contains(UmaSoulUtils.getGrowth(stack)) && last >= laterThen;
    }

    public double getUmaFactorCorrection(ItemStack stack, Level world) {
        int[] propertiesAsLevel = UmaSoulUtils.getProperty(stack).array();
        int fieldSituation = world.getRandom().nextIntBetweenInclusive(0, 3);
        Identifier nameLoc = UmaSoulUtils.getName(stack);
        UmaData umaData = UmapyoiAPI.getUmaDataRegistry(world).getOptional(nameLoc).orElseGet(() -> {
            Umapyoi.getLogger().info("Warning: {} doesn't exist.", nameLoc);
            return UmaData.DEFAULT_UMA;
        });
        Position umaPosition = umaData.position();
        double distanceFactor = this.distance.GetMultiplier(stack);
        double surfaceFactor = this.surface.GetMultiplier(stack);
        double fieldSituationFactor = 1 - (fieldSituation * 0.05d); // last factor
        Motivations motivation = UmaSoulUtils.getMotivation(stack);

        double totalProperties = propertiesAsLevel[0] * (2 - umaPosition.speedFactor) * this.correction[0] + propertiesAsLevel[1]
                * (2 - umaPosition.staminaFactor) * this.correction[1] + propertiesAsLevel[2] * this.correction[2] +
                propertiesAsLevel[3] * this.correction[3] + propertiesAsLevel[4] * this.correction[4];
        return totalProperties / this.referenceLevel * motivation.getMultiplier() * surfaceFactor * distanceFactor * fieldSituationFactor;
    }

    public double getSelfProp(ItemStack stack, Level world) {
        Identifier nameLoc = UmaSoulUtils.getName(stack);
        UmaData umaData = UmapyoiAPI.getUmaDataRegistry(world).getOptional(nameLoc).orElseGet(() -> {
            Umapyoi.getLogger().info("Warning: {} doesn't exist.", nameLoc);
            return UmaData.DEFAULT_UMA;
        });
        var propertiesAsLevel = UmaSoulUtils.getProperty(stack).array();
        Position umaPosition = umaData.position();
        double totalProperties = propertiesAsLevel[0] * (2 - umaPosition.speedFactor) * this.correction[0] + propertiesAsLevel[1]
                * (2 - umaPosition.staminaFactor) * this.correction[1] + propertiesAsLevel[2] * this.correction[2] +
                propertiesAsLevel[3] * this.correction[3] + propertiesAsLevel[4] * this.correction[4];
        Motivations motivation = UmaSoulUtils.getMotivation(stack);
        return totalProperties * motivation.getMultiplier();
    }

    public double offScalar(ItemStack stack, Level world) {
        double selfProp = this.getSelfProp(stack, world);
        return Math.min(selfProp, this.referenceLevel) / Math.max(selfProp, this.referenceLevel);
    }

    public boolean isPassed(ItemStack stack, Level world) {
        return this.getSelfProp(stack, world) >= this.referenceLevel;
    }

    public void followUp(ItemStack stack, Level level) {
        var raceData = UmaSoulUtils.getRaceStatus(stack);

        var wonRaces = raceData.wonRaces();
        if (this.isPassed(stack, level)) {
            tags.stream().map(UmapyoiAPI.getRaceTagRegistry(level)::get)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(Holder::value)
                    .forEach((t) -> t.applyToUmaSoul(stack, this));
            wonRaces = new HashSet<>(wonRaces);
            wonRaces.add(this.id);
        }

        var attended = new HashMap<>(raceData.attended());
        int count = attended.getOrDefault(this.id, 0) + 1;
        attended.put(this.id, count);

        int lastAttend = raceData.lastAttendTime();
        int newLastAttend = this.year.stream()
                .filter(y -> (y.ordinal() * 24 + this.time) > lastAttend)
                .min(Comparator.naturalOrder())
                .map(y -> y.ordinal() * 24 + this.time)
                .orElse(lastAttend + 1);

        boolean hasDebut = raceData.hasDebut();
        if (this.ranking == RaceRanking.DEBUT) {
            hasDebut = true;
        }

        var newRaceData = new UmaDataRaceStatus(wonRaces, attended, newLastAttend, hasDebut,
                raceData.attendRaceTag(), raceData.attendRaceTagUnique());
        UmaSoulUtils.setRaceStatus(stack, newRaceData);
    }

    public static class RaceBuilder {
        private RaceRanking ranking;
        private int length;
        private int time;
        private final Set<Year> year;
        private final Set<Identifier> tags;
        private Surface surface;
        private Identifier field;
        private final Set<Integer> attrCorr;
        private Integer referenceLevel;
        private boolean exclusive;
        private Set<Growth> allowStatus;
        private int later;
        private Set<Identifier> afterRace;
        private String texturePredicateOverride;

        public RaceBuilder() {
            this.ranking = RaceRanking.DEBUT;
            this.length = 0;
            this.time = 1;
            this.surface = Surface.TURF;
            this.year = new HashSet<>();
            this.tags = new HashSet<>();
            this.field = Identifier.fromNamespaceAndPath(Umapyoi.MODID, "unknown");
            this.attrCorr = new HashSet<>();
            this.referenceLevel = null;
            this.exclusive = true;
            this.allowStatus = new HashSet<>(List.of(Growth.TRAINED, Growth.RETIRED));
            this.later = 0;
            this.afterRace = new HashSet<>();
            this.texturePredicateOverride = null;
        }

        public RaceBuilder setLength(int len) {
            this.length = len;
            return this;
        }

        public RaceBuilder setRanking(RaceRanking rank) {
            this.ranking = rank;
            return this;
        }

        public RaceBuilder setTime(int time) {
            this.time = time;
            return this;
        }

        public RaceBuilder setTime(int month, boolean isLatter) {
            return this.setTime((month - 1) * 2 + (isLatter ? 2 : 1));
        }

        public RaceBuilder addYear(Year... years) {
            Collections.addAll(this.year, years);
            return this;
        }

        public RaceBuilder addTags(Identifier... locs) {
            Collections.addAll(this.tags, locs);
            return this;
        }

        @SafeVarargs
        public final RaceBuilder addTags(ResourceKey<RaceTag>... locs) {
            Arrays.stream(locs).map(ResourceKey::identifier).forEach(this::addTags);
            return this;
        }

        public RaceBuilder setSurface(Surface surface) {
            this.surface = surface;
            return this;
        }

        public RaceBuilder setField(String field) {
            return this.setField(Identifier.fromNamespaceAndPath(Umapyoi.MODID, field));
        }

        public RaceBuilder setField(ResourceKey<RaceField> field) {
            return this.setField(field.identifier());
        }

        public RaceBuilder setField(Identifier field) {
            this.field = field;
            return this;
        }

        public RaceBuilder addAttr(int... attrs) {
            for (int v: attrs) this.attrCorr.add(v);
            return this;
        }

        public RaceBuilder setReferenceLevel(int referenceLevel) {
            this.referenceLevel = referenceLevel;
            return this;
        }

        public RaceBuilder setAllowStatus(Growth... growth) {
            this.allowStatus = Arrays.stream(growth).collect(Collectors.toSet());
            return this;
        }

        public RaceBuilder addAllowStatus(Growth... growth) {
            this.allowStatus.addAll(Arrays.stream(growth).toList());
            return this;
        }

        public RaceBuilder setExclusive(boolean exclusive) {
            this.exclusive = exclusive;
            return this;
        }

        public RaceBuilder onlyIfLaterThen(int time) {
            this.later = time;
            return this;
        }

        public RaceBuilder onlyIfLaterThen(Identifier... races){
            this.afterRace.addAll(Arrays.stream(races).toList());
            return this;
        }

        public RaceBuilder setTexture(String texturePredicateOverride) {
            this.texturePredicateOverride = texturePredicateOverride;
            return this;
        }

        public Race create(Identifier id) {
            return new Race(id, this.ranking, this.length, this.time, this.year, this.surface, this.tags, this.field,
                    this.attrCorr, Optional.ofNullable(this.referenceLevel).orElseGet(() ->
                    switch (this.ranking) {
                        case DEBUT, PREOP -> 5;
                        case OP -> 15;
                        case GIII -> 25;
                        case GII -> 30;
                        case GI -> 35;
                    }
            ), this.allowStatus, this.exclusive, this.later, this.afterRace, this.texturePredicateOverride);
        }
    }
}
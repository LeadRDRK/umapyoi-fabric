package net.tracen.umapyoi.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.item.UmaRaceTicketItem;
import net.tracen.umapyoi.registry.races.Race;
import net.tracen.umapyoi.utils.RaceRanking;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record RaceTicketRandomLootFunction(RaceRanking least, RaceRanking most, boolean mode,
                                           Set<String> predicate) implements LootItemFunction {

    public static final MapCodec<RaceTicketRandomLootFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(RaceRanking.CODEC.optionalFieldOf("least", RaceRanking.DEBUT).forGetter(RaceTicketRandomLootFunction::least),
                    RaceRanking.CODEC.optionalFieldOf("most", RaceRanking.GI).forGetter(RaceTicketRandomLootFunction::most),
                    Codec.BOOL.optionalFieldOf("mode", true).forGetter(RaceTicketRandomLootFunction::mode),
                    Codec.STRING.listOf().<Set<String>>xmap(HashSet::new, s -> s.stream().toList()).optionalFieldOf("predicate", Collections.emptySet()).forGetter(RaceTicketRandomLootFunction::predicate))
            .apply(instance, RaceTicketRandomLootFunction::new));

    @Override
    public LootItemFunctionType<?> getType() {
        return LootFunctionRegistry.RACE_TICKET_RANDOM.get();
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext lootContext) {
        RandomSource rand = lootContext.getRandom();
        ServerLevel level = lootContext.getLevel();
        List<Race> raceListOfPredicate = UmapyoiAPI.getRaceRegistry(level).stream()
                .filter(i -> least.compareTo(i.ranking()) <= 0 && i.ranking().compareTo(most) <= 0)
                .filter(i -> predicate.isEmpty() || (mode == predicate.contains(i.texturePredicateOverride)))
                .toList();
        if (raceListOfPredicate.isEmpty()) return ItemStack.EMPTY;
        Race raceDeterm = raceListOfPredicate.get(rand.nextInt(raceListOfPredicate.size()));
        ResourceLocation id = raceDeterm.id;
        var race = lootContext.getResolver().lookupOrThrow(Race.REGISTRY_KEY)
                .get(ResourceKey.create(Race.REGISTRY_KEY, id))
                .map(Holder::value)
                .orElse(null);
        return UmaRaceTicketItem.init(id, race, stack);
    }
}

package net.tracen.umapyoi.data.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.registry.races.Race;
import net.tracen.umapyoi.utils.RaceRanking;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RaceTicketRandomLootFunction implements LootItemFunction {
    public final RaceRanking least;
    public final RaceRanking most;
    public final boolean mode;
    public final Set<String> predicate;
    public RaceTicketRandomLootFunction(RaceRanking least, RaceRanking most, boolean mode, Set<String> predicate) {
        this.least = least;
        this.most = most;
        this.mode = mode;
        this.predicate = predicate;
    }

    public static final Codec<RaceTicketRandomLootFunction> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(RaceRanking.CODEC.optionalFieldOf("least", RaceRanking.DEBUT).forGetter(RaceTicketRandomLootFunction::getLeast),
                    RaceRanking.CODEC.optionalFieldOf("most", RaceRanking.GI).forGetter(RaceTicketRandomLootFunction::getMost),
                    Codec.BOOL.optionalFieldOf("mode", true).forGetter(RaceTicketRandomLootFunction::getMode),
                    Codec.STRING.listOf().<Set<String>>xmap(HashSet::new, s -> s.stream().toList()).optionalFieldOf("predicate", Collections.emptySet()).forGetter(RaceTicketRandomLootFunction::getPredicate))
            .apply(instance, RaceTicketRandomLootFunction::new));

    @Override
    public LootItemFunctionType getType() {
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
        stack.getOrCreateTag().putString("race", id.toString());
        return stack;
    }

    public RaceRanking getLeast() {
        return least;
    }

    public RaceRanking getMost() {
        return most;
    }

    public boolean getMode() {
        return mode;
    }

    public Set<String> getPredicate() {
        return predicate;
    }
}

package net.tracen.umapyoi.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.UmaSkillRegistry;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UmaSkillLootFunction extends LootItemConditionalFunction {
    private final Optional<Set<Identifier>> skills;
    private int level;

    public static final MapCodec<UmaSkillLootFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance)
            .and(instance.group(
                    Identifier.CODEC.listOf().xmap(Set::copyOf, List::copyOf).optionalFieldOf("skills").forGetter(UmaSkillLootFunction::getSkills),
                    Codec.INT.fieldOf("level").forGetter(UmaSkillLootFunction::getLevel)))
            .apply(instance, UmaSkillLootFunction::new));

    public UmaSkillLootFunction(List<LootItemCondition> predicates, Optional<Set<Identifier>> skills, int level) {
        super(predicates);
        this.skills = skills;
        this.level = level;
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
    }

    public Optional<Set<Identifier>> getSkills() {
        return skills;
    }

    public int getLevel() {
        return level;
    }

    public static <T> Builder<?> setSkillLevel(int level) {
        return simpleBuilder(predicates -> new UmaSkillLootFunction(predicates, Optional.empty(), level));
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        try {
            RandomSource random = context.getRandom();
            List<Identifier> list = this.skills
                    .orElseGet(() -> UmaSkillRegistry.REGISTRY.get().keySet())
                    .stream()
                    .filter(e -> UmaSkillRegistry.REGISTRY.get()
                            .get(e)
                            .map(Holder::value)
                            .map(skill -> skill.getSkillLevel() == level)
                            .orElse(false))
                    .toList();
            Optional<Identifier> optional = Util.getRandomSafe(list, random);
            if (optional.isEmpty()) {
                Umapyoi.getLogger().warn("Couldn't find a compatible skill for {}", stack);
            } else {
                Identifier skill = optional.get();
                if(stack.is(ItemRegistry.SKILL_BOOK)) {
                    stack.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), skill);
                }
            }
            return stack; } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

package net.tracen.umapyoi.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.UmaSkillRegistry;

import java.util.*;

public class UmaSkillLootFunction extends LootItemConditionalFunction {
    private final Optional<Set<ResourceLocation>> skills;
    private int level;

    public static final MapCodec<UmaSkillLootFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance)
            .and(instance.group(
                    ResourceLocation.CODEC.listOf().xmap(Set::copyOf, List::copyOf).optionalFieldOf("skills").forGetter(UmaSkillLootFunction::getSkills),
                    Codec.INT.fieldOf("level").forGetter(UmaSkillLootFunction::getLevel)))
            .apply(instance, UmaSkillLootFunction::new));

    public UmaSkillLootFunction(List<LootItemCondition> predicates, Optional<Set<ResourceLocation>> skills, int level) {
        super(predicates);
        this.skills = skills;
        this.level = level;
    }

    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return LootFunctionRegistry.UMASKILL_WITH_LEVEL.get();
    }

    public Optional<Set<ResourceLocation>> getSkills() {
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
            List<ResourceLocation> list = this.skills
                    .orElseGet(() -> UmaSkillRegistry.REGISTRY.get().keySet())
                    .stream()
                    .filter(e -> UmaSkillRegistry.REGISTRY.get().get(e).getSkillLevel() == level)
                    .toList();
            Optional<ResourceLocation> optional = Util.getRandomSafe(list, random);
            if (optional.isEmpty()) {
                Umapyoi.getLogger().warn("Couldn't find a compatible skill for {}", stack);
            } else {
                ResourceLocation skill = optional.get();
                if(stack.is(ItemRegistry.SKILL_BOOK.get())) {
                    stack.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), skill);
                }
            }
            return stack; } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

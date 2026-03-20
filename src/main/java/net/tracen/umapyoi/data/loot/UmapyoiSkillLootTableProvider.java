package net.tracen.umapyoi.data.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.loot.UmaSkillLootFunction;

import java.util.function.BiConsumer;

public class UmapyoiSkillLootTableProvider extends SimpleFabricLootTableProvider {
    public static final ResourceLocation COMPLEX_SKILLS = register("complex_skills");
    public static final ResourceLocation SIMPLE_SKILLS = register("simple_skills");

    public UmapyoiSkillLootTableProvider(FabricDataOutput output) {
        super(output, LootContextParamSets.CHEST);
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(SIMPLE_SKILLS, LootTable.lootTable().withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(0.0F, 4.0F))
                        .conditionally(LootItemRandomChanceCondition.randomChance(0.15f).build())
                        .add(LootItem.lootTableItem(ItemRegistry.SKILL_BOOK.get()).setWeight(10).apply(UmaSkillLootFunction.setSkillLevel(1)))
                        .add(LootItem.lootTableItem(ItemRegistry.SKILL_BOOK.get()).setWeight(10).apply(UmaSkillLootFunction.setSkillLevel(1)))
                )
        );

        consumer.accept(COMPLEX_SKILLS, LootTable.lootTable().withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(0.0F, 2.0F))
                        .conditionally(LootItemRandomChanceCondition.randomChance(0.5f).build())
                        .add(LootItem.lootTableItem(ItemRegistry.SKILL_BOOK.get()).setWeight(10).apply(UmaSkillLootFunction.setSkillLevel(2)))
                        .add(LootItem.lootTableItem(ItemRegistry.SKILL_BOOK.get()).setWeight(10).apply(UmaSkillLootFunction.setSkillLevel(2)))
                )
        );
    }

    private static ResourceLocation register(String name) {
        return Umapyoi.id(name);
    }
}

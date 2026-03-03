package net.tracen.umapyoi.data.loot;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

import java.util.Set;

public interface AddLootTableModifier {
    Set<ResourceKey<LootTable>> targetLootTables();
    ResourceKey<LootTable> lootTable();

    AddLootTableModifier[] MODIFIERS = new AddLootTableModifier[] {
            new RareSkillBooksLoot(),
            new SkillBooksLoot(),
            new TrainingBooksLoot(),
            new RaceTicketUpToOpLoot(),
            new RaceTicketUpToGILoot(),
            new RaceTicketUpToGIILoot(),
            new RaceTicketUpToGIIILoot(),
            new RaceTicketEndLoot()
    };

    class ModifyLootTableListener implements LootTableEvents.Modify {
        @Override
        public void modifyLootTable(ResourceKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source) {
            for (var modifier : MODIFIERS) {
                if (modifier.targetLootTables().contains(key)) {
                    tableBuilder.pool(
                            LootPool.lootPool()
                                    .add(NestedLootTable.lootTableReference(modifier.lootTable()))
                                    .build()
                    );
                }
            }
        }
    }

    static void registerListeners() {
        LootTableEvents.MODIFY.register(new ModifyLootTableListener());
    }
}

package net.tracen.umapyoi.data.loot;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Set;

public interface AddLootTableModifier {
    Set<ResourceLocation> targetLootTables();
    ResourceLocation lootTable();

    AddLootTableModifier[] MODIFIERS = new AddLootTableModifier[] {
            new RareSkillBooksLoot(),
            new SkillBooksLoot(),
            new TrainingBooksLoot()
    };

    class ModifyLootTableListener implements LootTableEvents.Modify {
        @Override
        public void modifyLootTable(ResourceManager resourceManager, LootDataManager lootManager,
                                    ResourceLocation id, LootTable.Builder tableBuilder, LootTableSource source) {
            for (var modifier : MODIFIERS) {
                if (modifier.targetLootTables().contains(id)) {
                    var extraTable = lootManager.getLootTable(modifier.lootTable());
                    for (var pool : extraTable.pools) {
                        tableBuilder.pool(pool);
                    }
                }
            }
        }
    }

    static void registerListeners() {
        LootTableEvents.MODIFY.register(new ModifyLootTableListener());
    }
}
